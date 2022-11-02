package site.xleon.platform.config.app;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import site.xleon.platform.models.SysPermission;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "config")
public class AppConfig {
    private String dateFormat;

    private Integer pageSize;
    private Long pageSizeMax;

    private JWTConfig jwt;

    @Bean
    public SysPermission[] appPermissions() throws IOException {
        Resource resource = new ClassPathResource("appPermission.json");
        String jsonString = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
        List<SysPermission> permissions = JSON.parseArray(jsonString, SysPermission.class);
        return permissions.toArray(new SysPermission[0]);
    }
}
