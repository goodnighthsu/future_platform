package site.xleon.platform.controllers;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import site.xleon.platform.core.IdParams;
import site.xleon.platform.core.MyException;
import site.xleon.platform.core.Result;
import site.xleon.platform.core.enums.StateEnum;
import site.xleon.platform.mapper.SysRoleMapper;
import site.xleon.platform.mapper.SysRolePermissionMapper;
import site.xleon.platform.mapper.SysUserMapper;
import site.xleon.platform.mapper.impl.SysRolePermissionServiceImpl;
import site.xleon.platform.models.SysPermission;
import site.xleon.platform.models.SysRoleEntity;
import site.xleon.platform.models.SysRolePermissionEntity;
import site.xleon.platform.models.SysUserEntity;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/sysRole")
@AllArgsConstructor
public class SysRoleController extends BaseController {

    private final SysRoleMapper sysRoleMapper;

    private final SysRolePermissionMapper sysRolePermissionMapper;

    private final SysRolePermissionServiceImpl sysRolePermissionService;

    private final SysUserMapper sysUserMapper;

    @GetMapping()
    public Result<Page<SysRoleEntity>> list(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) {
        Page<SysRoleEntity> paging = utils.page(page, pageSize);
        QueryWrapper<SysRoleEntity> query = new QueryWrapper<>();
        query.select(SysRoleEntity.class, item -> !"password".equals(item.getColumn())).orderByDesc("id");
        Page<SysRoleEntity> roles = sysRoleMapper.selectPage(paging, query);
        return Result.success(roles);
    }

    @Data
    private static class AddParams {
        @NotBlank
        private String title;

        @Nullable
        private String detail;

        private StateEnum state = StateEnum.DISABLE;
    }

    /**
     * 添加用户角色
     *
     * @param params AddParams
     * @return role
     * @throws MyException
     */
    @PostMapping()
    public Result<SysRoleEntity> add(@RequestBody @Valid AddParams params) throws MyException {
        String title = params.getTitle().trim();
        LambdaQueryWrapper<SysRoleEntity> query = new LambdaQueryWrapper<>();
        query.select(SysRoleEntity::getId)
                .eq(SysRoleEntity::getTitle, title);
        SysRoleEntity existRole =sysRoleMapper.selectOne(query);
        if (existRole != null) {
            throw new MyException("role " + title + " already exist");
        }

        String detail = params.getDetail() == null ? null : params.getDetail().trim();

        SysRoleEntity role = new SysRoleEntity();
        role.setTitle(title);
        role.setDetail(detail);
        role.setState(params.getState());

        int count = sysRoleMapper.insert(role);
        if (count != 1) {
            throw new MyException("add role failure");
        }

        return Result.success(role);
    }

    /**
     * 修改角色
     * @param role role
     * @return updated
     * @throws MyException exception
     */
    @PutMapping()
    public Result<SysRoleEntity> update(@RequestBody SysRoleEntity role) throws MyException {
        if (role.getId() == null) {
            throw new MyException("Update role, id can not be null");
        }

        SysRoleEntity localRole = sysRoleMapper.selectById(role.getId());
        if (localRole == null) {
            throw new MyException("Can not found role by id " + role.getId());
        }

        String title = role.getTitle().trim();
        LambdaQueryWrapper<SysRoleEntity> query = new LambdaQueryWrapper<>();
        query.select(SysRoleEntity::getId)
                .eq(SysRoleEntity::getTitle, title);
        SysRoleEntity existRole =sysRoleMapper.selectOne(query);
        if (existRole != null) {
            throw new MyException("role " + title + " already exist");
        }

        int count = sysRoleMapper.updateById(role);

        if (count <= 0) {
            throw  new MyException("update role failure");
        }

        return Result.success(role);
    }

    @DeleteMapping
    public Result<List<String>> deleteRole(@RequestBody IdParams params) throws MyException {
        QueryWrapper<SysUserEntity> queryUser = new QueryWrapper<>();
        queryUser.select("id").in("role_id", params.getIds());
        List<SysUserEntity> users = sysUserMapper.selectList(queryUser);
        if (!users.isEmpty()) {
            throw new MyException("role is using");
        }

        if (sysRoleMapper.deleteBatchIds(params.getIds()) == 0) {
            throw new MyException(("delete failure"));
        }

        return Result.success((params.getIds()));
    }

    @GetMapping("/{id}/permission")
    public Result<SysPermission[]> listRolePermission(@PathVariable Integer id) throws IOException {
        SysPermission[] rolePermissions = sysRolePermissionService.permissions(id);
        return Result.success(rolePermissions);
    }

    @Data
    private static class UpdatePermissionParams {
        private List<String> permissions;
    }

    @PutMapping("/{id}/permission")
    public Result<List<String>> updatePermission(@PathVariable Integer id, @RequestBody UpdatePermissionParams params){
        UpdateWrapper<SysRolePermissionEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("role_id", id);
        SysRolePermissionEntity rolePermission = new SysRolePermissionEntity();
        rolePermission.setState(StateEnum.NONE);
        sysRolePermissionMapper.update(rolePermission, updateWrapper);

        List<SysRolePermissionEntity> rolePermissions = new ArrayList<>();
        for (String permission :
                params.getPermissions()) {
            LambdaQueryWrapper<SysRolePermissionEntity> query = new LambdaQueryWrapper<>();
            query.eq(SysRolePermissionEntity::getRoleId, id).eq(SysRolePermissionEntity::getPermission, permission);
            SysRolePermissionEntity existPermission = sysRolePermissionMapper.selectOne(query);
            if (existPermission == null) {
                existPermission = new SysRolePermissionEntity();
                existPermission.setRoleId(id);
                existPermission.setPermission(permission);
            }

            existPermission.setState(StateEnum.ENABLE);
            rolePermissions.add(existPermission);
        }

        sysRolePermissionService.saveOrUpdateBatch(rolePermissions);
        return Result.success(params.permissions);
    }
}
