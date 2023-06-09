package site.xleon.platform.controllers;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import site.xleon.platform.core.IdParams;
import site.xleon.platform.core.MyException;
import site.xleon.platform.core.Result;
import site.xleon.platform.core.enums.StateEnum;
import site.xleon.platform.mapper.SysRoleMapper;
import site.xleon.platform.mapper.SysUserMapper;
import site.xleon.platform.mapper.impl.SysRolePermissionServiceImpl;
import site.xleon.platform.models.SysPermission;
import site.xleon.platform.models.SysRoleEntity;
import site.xleon.platform.models.SysUserEntity;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/sysUser")
@AllArgsConstructor
public class SysUserController extends BaseController {

    private final SysUserMapper sysUserMapper;

    private final SysRoleMapper sysRoleMapper;

    private final SysRolePermissionServiceImpl rolePermissionServiceImpl;


    @Data
    public static class LoginParams {
        @NotBlank
        private String account;

        @NotBlank
        private String password;
    }

    /**
     * 用户登录
     * @param params login params
     * @return sys user
     */
    @PostMapping("/login")
    public Result<SysUserEntity> login(@RequestBody @Valid  LoginParams params) throws MyException {
        LambdaQueryWrapper<SysUserEntity> query = new LambdaQueryWrapper<>();
        query.select(SysUserEntity::getId, SysUserEntity::getState)
                .eq(SysUserEntity::getAccount, params.getAccount())
                .eq(SysUserEntity::getPassword, params.getPassword());
        SysUserEntity user = sysUserMapper.selectOne(query);
        if (user == null) {
            throw new MyException("account or password invalid");
        }

        if (!StateEnum.ENABLE.getValue().equals(user.getState().getValue())) {
            throw new MyException("account disable");
        }

        user.setToken(jwt.createByUser(user, true));
        user.setId(null);
        return Result.success(user);
    }

    @GetMapping()
    public Result<Page<SysUserEntity>> list(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) {
        Page<SysUserEntity> paging = utils.page(page, pageSize);
        QueryWrapper<SysUserEntity> query = new QueryWrapper<>();
        query.select(SysUserEntity.class, item -> !"password".equals(item.getColumn())).orderByDesc("id");
        Page<SysUserEntity> users = sysUserMapper.selectPage(paging, query);
        users.getRecords().forEach( user -> {
            QueryWrapper<SysRoleEntity> queryRole = new QueryWrapper<>();
            queryRole.select(SysRoleEntity.class,
                    item -> "id".equals(item.getColumn()) || "title".equals(item.getColumn())
            ).orderByDesc("title");
            SysRoleEntity role = sysRoleMapper.selectById(user.getRoleId());
            user.setRole(role);
        });
        return Result.success(users);
    }

    @Data
    private static class AddParams {
        @NotBlank
        private String account;

        private String mobile;

        @NotBlank
        private String roleId;

        @NotBlank
        private String password;

        private String detail;

        private String state;
    }

    /**
     * 添加用户
     *
     * @param params AddParams
     * @return role
     * @throws MyException exception
     */
    @PostMapping()
    public Result<SysUserEntity> add(@RequestBody @Valid AddParams params) throws MyException {
        // check account
        String title = params.getAccount().trim();
        LambdaQueryWrapper<SysUserEntity> query = new LambdaQueryWrapper<>();
        query.select(SysUserEntity::getId)
                .eq(SysUserEntity::getAccount, title);
        SysUserEntity existUser = sysUserMapper.selectOne(query);
        if (existUser != null) {
            throw new MyException("Account " + title + " already exist");
        }

        // check role
        String roleId = params.getRoleId().trim();
        SysRoleEntity existRole = sysRoleMapper.selectById(roleId);
        if (existRole == null) {
            throw new MyException("Invalid role id" + roleId);
        }

        String mobile = params.getMobile() == null ? null : params.getMobile().trim();
        String detail = params.getDetail() == null ? null : params.getDetail().trim();
        StateEnum state = "1".equals(params.getState()) ? StateEnum.ENABLE : StateEnum.DISABLE;

        SysUserEntity entity = new SysUserEntity();
        entity.setAccount(title);
        entity.setMobile(mobile);
        entity.setRoleId(Integer.parseInt(params.getRoleId()));
        entity.setPassword(params.getPassword());
        entity.setDetail(detail);
        entity.setState(state);

        int count = sysUserMapper.insert(entity);
        if (count != 1) {
            throw new MyException("Add user failure");
        }

        return Result.success(entity);
    }

    /**
     * 用户详情
     * @param id user id
     * @return user info detail
     */
    @GetMapping("/{id}")
    public Result<SysUserEntity> detail(@PathVariable String id) {
        SysUserEntity user = sysUserMapper.selectById(Integer.parseInt(id));
        user.setPassword(null);
        return Result.success(user);
    }

    @GetMapping("/current")
    public Result<SysUserEntity> current() throws MyException, IOException {
        SysUserEntity user = sysUserMapper.selectById(getUserId());
        user.setPassword(null);
        SysPermission[] permissions = rolePermissionServiceImpl.permissions(user.getRoleId());
        user.setPermissions(permissions);
        return Result.success(user);
    }

    /**
     * 修改账号
     * @param user user
     * @return updated
     * @throws MyException exception
     */
    @PutMapping()
    public Result<SysUserEntity> update(@RequestBody SysUserEntity user) throws MyException {
        if (user.getId() == null) {
            throw new MyException("update user, id can not be null");
        }
        SysUserEntity localUser = sysUserMapper.selectById(user.getId());
        if (localUser == null) {
            throw new MyException("can not found user by id " + user.getId());
        }

        int count = sysUserMapper.updateById(user);

        if (count <= 0) {
            throw  new MyException("update user failure");
        }

        return Result.success(user);
    }

    @DeleteMapping
    public Result<List<String>> deleteUser(@RequestBody IdParams params) throws MyException {
        if (sysUserMapper.deleteBatchIds(params.getIds()) == 0) {
            throw new MyException(("delete failure"));
        }
        return Result.success((params.getIds()));
    }
}
