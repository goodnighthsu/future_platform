package site.xleon.platform.controllers;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
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

    /**
     * 获取分页角色列表
     *
     * @param page page
     * @param pageSize page size
     * @return 角色列表
     */
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
     * @throws MyException exception
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

    /**
     * 批量删除角色
     * @param params 角色id组
     * @return 被删除的id组
     * @throws MyException exception
     */
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

    /**
     * 获取角色权限
     * @param id 角色id
     * @return 权限组
     * @throws IOException exception
     */
    @GetMapping("/{id}/permission")
    public Result<SysPermission[]> listRolePermission(@PathVariable Integer id) throws IOException {
        SysPermission[] rolePermissions = sysRolePermissionService.permissions(id);
        return Result.success(rolePermissions);
    }

    /**
     * 更新角色权限配置
     *
     * @param roleId role id
     * @param params 允许的权限名数组
     * @return role id
     */
    @PutMapping("/{roleId}/permission")
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> updatePermission(
            @PathVariable Integer roleId,
            @RequestBody List<String> params){
        // 首先移除角色权限配置
        LambdaQueryWrapper<SysRolePermissionEntity> queryRolePermission = new LambdaQueryWrapper<>();
        queryRolePermission.eq(SysRolePermissionEntity::getRoleId, roleId);
        sysRolePermissionMapper.delete(queryRolePermission);

        List<SysRolePermissionEntity> rolePermissions = new ArrayList<>();
        for (String permission :
                params) {
            LambdaQueryWrapper<SysRolePermissionEntity> query = new LambdaQueryWrapper<>();
            query.eq(SysRolePermissionEntity::getRoleId, roleId)
                    .eq(SysRolePermissionEntity::getPermission, permission);
            SysRolePermissionEntity existPermission = sysRolePermissionMapper.selectOne(query);
            if (existPermission == null) {
                existPermission = new SysRolePermissionEntity();
                existPermission.setRoleId(roleId);
                existPermission.setPermission(permission);
            }

            existPermission.setState(StateEnum.ENABLE);
            rolePermissions.add(existPermission);
        }

        sysRolePermissionService.saveBatch(rolePermissions);
        return Result.success(roleId);
    }
}
