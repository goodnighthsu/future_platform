package site.xleon.platform.config.druid;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DynamicDataSourceConfig {
    @Bean
    @ConfigurationProperties("spring.datasource.druid.master")
    public DataSource dataSourceMaster() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public DynamicDataSource dataSource() {
        Map<Object, Object> dataSources = new HashMap<>();
        dataSources.put("master", dataSourceMaster());
        return new DynamicDataSource(dataSourceMaster(), dataSources);
    }
}
