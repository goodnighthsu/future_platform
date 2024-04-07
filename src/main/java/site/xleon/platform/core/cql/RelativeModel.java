package site.xleon.platform.core.cql;

import com.google.common.base.CaseFormat;
import lombok.Data;
import site.xleon.platform.core.cql.BaseEntity;

@Data
public class RelativeModel {
    private String id;
    public String getId() {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, this.id);
    }

    private String foreignId;
    public String getForeignId() {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, this.foreignId);
    }

    private Class<? extends BaseEntity> entityClass;
}
