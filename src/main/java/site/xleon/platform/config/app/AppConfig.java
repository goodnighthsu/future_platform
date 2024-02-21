package site.xleon.platform.config.app;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import site.xleon.platform.models.SysPermission;

@Data
@Configuration
@ConfigurationProperties(prefix = "config")
public class AppConfig {
    private String dateFormat;

    private Integer pageSize;
    private Long pageSizeMax;

    private JWTConfig jwt;

    private SysPermission[] appPermissions;
}
