package site.xleon.platform.models;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * base entity
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
@Data
public class BaseEntity {

    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String DEFAULT_TIMEZONE = "GMT+8";


    @JsonFormat(pattern = DEFAULT_DATE_PATTERN, timezone = DEFAULT_TIMEZONE)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @JsonFormat(pattern = DEFAULT_DATE_PATTERN, timezone = DEFAULT_TIMEZONE)
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
}
