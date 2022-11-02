package site.xleon.platform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;
import site.xleon.platform.config.druid.DynamicDataSourceConfig;

@MapperScan("site.xleon.platform.mapper")
@Import({DynamicDataSourceConfig.class})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class PlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlatformApplication.class, args);
	}

}
