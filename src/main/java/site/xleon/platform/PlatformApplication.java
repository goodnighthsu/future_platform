package site.xleon.platform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;
import site.xleon.platform.config.druid.DynamicDataSourceConfig;
import site.xleon.platform.core.NettyServer;

@MapperScan("site.xleon.platform.mapper")
@Import({DynamicDataSourceConfig.class})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class PlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlatformApplication.class, args);
		try {
			new NettyServer(33333).run();
		}catch (Exception e) {
			System.out.println("netty server error: " + e.toString());
		}
	}
}
