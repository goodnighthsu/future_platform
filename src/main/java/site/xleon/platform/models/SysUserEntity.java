package site.xleon.platform.models;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import site.xleon.platform.core.cql.TableRelative;
import site.xleon.platform.core.cql.BaseEntity;
import site.xleon.platform.core.enums.StateEnum;

/**
 * 系统用户
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "sys_user")
public class SysUserEntity extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String account;

    private String password;

    private String detail;

    private String mobile;

    private StateEnum state;

    private Integer roleId;

    @TableField(exist = false)
    @TableRelative(id="roleId")
    private SysRoleEntity role;

    @TableField(exist = false)
    private String token;

    @TableField(exist = false)
    private SysPermission[] permissions;
}
