package site.xleon.platform.core.cql;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.CaseFormat;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ModelEnum {
    SYS_USER("sysUser", "SysUserMapper", "SysUserEntity"),
    SYS_ROLE("sysRole", "SysRoleMapper", "SysRoleEntity");

    @EnumValue
    @JsonValue
    private final String value;
    public final String getValue() {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, value);
    }

    private final String mapper;
    public final String getMapper() {
        return "site.xleon.platform.mapper." + this.mapper;
    }

    private final String model;
    public final String getModel() {
        return "site.xleon.platform.models." + this.model;
    }
}
