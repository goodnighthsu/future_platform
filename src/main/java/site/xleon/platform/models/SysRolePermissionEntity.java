package site.xleon.platform.models;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import site.xleon.platform.core.enums.StateEnum;

@Data
@TableName("sys_role_permission")
public class SysRolePermissionEntity {
    private Integer roleId;

    private String permission;

    private StateEnum state;
}
