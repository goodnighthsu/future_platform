package site.xleon.platform.mapper.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.xleon.platform.mapper.SysRolePermissionMapper;
import site.xleon.platform.mapper.SysRolePermissionService;
import site.xleon.platform.models.SysRolePermissionEntity;

@Service()
public class SysRolePermissionServiceImpl
        extends ServiceImpl<SysRolePermissionMapper, SysRolePermissionEntity>
        implements SysRolePermissionService {
}
