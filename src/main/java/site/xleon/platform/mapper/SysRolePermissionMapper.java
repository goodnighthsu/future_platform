package site.xleon.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import site.xleon.platform.models.SysRolePermissionEntity;

public interface SysRolePermissionMapper extends BaseMapper<SysRolePermissionEntity> {
    @Select("SELECT * from sys_role_permission where role_id=#{roleId} and permission=#{permission} and state=1")
    SysRolePermissionEntity getAssigned(Integer roleId, String permission);
}
