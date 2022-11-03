package site.xleon.platform.mapper.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import site.xleon.platform.config.app.AppConfig;
import site.xleon.platform.core.enums.StateEnum;
import site.xleon.platform.mapper.SysRolePermissionMapper;
import site.xleon.platform.mapper.SysRolePermissionService;
import site.xleon.platform.models.SysPermission;
import site.xleon.platform.models.SysRolePermissionEntity;

import java.io.IOException;

@Service()
@AllArgsConstructor
public class SysRolePermissionServiceImpl
        extends ServiceImpl<SysRolePermissionMapper, SysRolePermissionEntity>
        implements SysRolePermissionService {

    private final AppConfig appConfig;

    public SysPermission[] permissions(Integer roleId) throws IOException {
        return rolePermission(appConfig.appPermissions(), roleId);
    }

    private SysPermission[] rolePermission(SysPermission[] permissions, Integer roleId) {
        for (SysPermission permission :
                permissions) {
            SysRolePermissionEntity isAssign = this.baseMapper.getAssigned(roleId, permission.getTitle());
            if (isAssign != null) {
                permission.setState(StateEnum.ENABLE);
            } else {
                permission.setState(StateEnum.DISABLE);
            }

            if (permission.getChildren() == null || permission.getChildren().length == 0) {
                continue;
            }

            rolePermission(permission.getChildren(), roleId);
        }

        return permissions;
    }
}
