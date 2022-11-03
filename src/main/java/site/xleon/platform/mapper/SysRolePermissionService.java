package site.xleon.platform.mapper;

import com.baomidou.mybatisplus.extension.service.IService;
import site.xleon.platform.models.SysPermission;
import site.xleon.platform.models.SysRolePermissionEntity;

import java.io.IOException;

public interface SysRolePermissionService extends IService<SysRolePermissionEntity> {
    /**
     * 角色权限
     * @param roleId role id
     * @return permissions
     */
    SysPermission[] permissions(Integer roleId) throws IOException;
}
