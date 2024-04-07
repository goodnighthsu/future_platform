package site.xleon.platform.core;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import site.xleon.platform.config.app.AppConfig;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Component
@AllArgsConstructor
@Slf4j
public class Utils {

    private final AppConfig appConfig;

    public <T> Page<T> page(Integer aPage, Integer aPageSize) {
        Integer page = Optional.ofNullable(aPage).orElse(1);
        Integer defaultPageSize = Optional.ofNullable(appConfig.getPageSize()).orElse(50);
        Integer pageSize = Optional.ofNullable(aPageSize).orElse(defaultPageSize);
        Page<T> paging = new Page<>(page, pageSize);
        paging.setMaxLimit(Optional.ofNullable(appConfig.getPageSizeMax()).orElse(1000L));
        return paging;
    }

    public static Map<String , Object> objectToMap(Object obj) {
        Map<String, Object> map = new LinkedHashMap<>();
        Class<?> clazz = obj.getClass();
        for (Field field: clazz.getDeclaredFields()) {
            field.setAccessible(true);

            try {
                Object value = field.get(obj);
                if (value == null) {
                    value = "";
                }
                map.put(field.getName(), value);
            }catch (Exception e) {
                log.error("error: ", e);
            }
        }

        return map;
    }

    public static class Reflect {
        public static Class<?> getFieldType(Class<?> clz, String filedName) {
            if (clz == null) return null;

            try {
                return clz.getDeclaredField(filedName).getType();
            }catch (NoSuchFieldException e) {
                log.debug("{} no such filed {}", clz.getName(), filedName);
                if (clz.getSuperclass() != null) {
                    return getFieldType(clz.getSuperclass(), filedName);
                }
            }


            return null;
        }
    }
}
