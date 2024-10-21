package org.xiaoxve.conf;

import lombok.Data;
import org.xiaoxve.component.LeftTab;
import org.xiaoxve.itfc.Tab;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * ConfTab 类用于配置标签页的属性，
 * 包含标签页的类名、名称、图标和启用状态。
 */
@Data
public class ConfTab {
    private String clazz; // 标签页的类名
    private String name; // 标签页的名称
    private String icon; // 标签页的图标路径
    private Boolean enabled; // 标签页是否启用

    /**
     * 生成标签页实例。
     *
     * @param leftTab 关联的 LeftTab 对象
     * @return 返回一个实现了 Tab 接口的标签页实例
     */
    public Tab generateInstance(LeftTab leftTab) {
        try {
            Class<?> tabClass = Class.forName(clazz); // 根据类名获取 Class 对象
            Constructor<?> constructor = tabClass.getConstructor(String.class, String.class, LeftTab.class); // 获取构造函数
            return (Tab) constructor.newInstance(name, icon, leftTab); // 创建并返回标签页实例
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e); // 捕获异常并抛出运行时异常
        }
    }
}
