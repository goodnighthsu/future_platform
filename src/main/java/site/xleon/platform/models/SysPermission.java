package site.xleon.platform.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.xleon.platform.core.enums.StateEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysPermission {
    private String title;

    private String detail;

    private String path;

    private boolean isMenu;

    private String[] api;

    private StateEnum state;

    private SysPermission[] children;
}
