package site.xleon.platform.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.xleon.platform.core.enums.StateEnum;

/**
 * 系统权限
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysPermission {
    /**
     * 权限名
     * eg: strategyList
     */
    private String title;

    /**
     * 权限显示信息
     * eg: 策略列表
     */
    private String detail;

    /**
     * 权限对应的菜单路径
     * 有则作为菜单项显示
     */
    private String path;

    /**
     * 权限允许调用的api
     */
    private String[] api;

    /**
     * 权限禁用/启用
     */
    private StateEnum state;

    /**
     * 子权限
     */
    private SysPermission[] children;
}
