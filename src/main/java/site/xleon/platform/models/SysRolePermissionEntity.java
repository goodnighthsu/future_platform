package site.xleon.platform.models;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import site.xleon.platform.core.enums.StateEnum;

@Data
@TableName("sys_role_permission")
public class SysRolePermissionEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer roleId;

    private String permission;

    private StateEnum state;
}
