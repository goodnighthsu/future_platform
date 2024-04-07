package site.xleon.platform.models;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import site.xleon.platform.core.cql.BaseEntity;
import site.xleon.platform.core.enums.StateEnum;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_role")
public class SysRoleEntity extends BaseEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String title;

    private String detail;

    private StateEnum state;
}