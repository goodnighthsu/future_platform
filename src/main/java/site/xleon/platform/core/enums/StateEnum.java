package site.xleon.platform.core.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum StateEnum {
    NONE(0, "none"),
    DISABLE(-1, "disable"),
    ENABLE(1, "enable");

    @EnumValue
    @JsonValue
    @Getter
    private final Integer value;

    @Getter
    private final String label;
}
