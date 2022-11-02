package site.xleon.platform.config.druid;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource {
    String value() default "master";
}
