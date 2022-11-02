package site.xleon.platform.config.druid;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

public class DynamicDataSource extends AbstractRoutingDataSource {
    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void setDataSource(String dataSource) { threadLocal.set(dataSource); }

    public static String getDataSource() { return threadLocal.get();}

    public static void clearDataSource() { threadLocal.remove(); }

    @Override
    protected Object determineCurrentLookupKey() {
        return null;
    }

    public DynamicDataSource(DataSource defaultDataSource, Map<Object, Object> targets) {
        super.setDefaultTargetDataSource(defaultDataSource);
        super.setTargetDataSources(targets);
        super.afterPropertiesSet();}
}
