package org.xiaoxve.itfc;

import javafx.scene.Node;

/**
 * Tab 接口定义了一个标签页的基本行为和特性。
 * 实现这个接口的类应提供标签页的具体实现。
 */
public interface Tab {

    /**
     * 获取标签页的 JavaFX 节点。
     *
     * @return 返回一个 Node 对象，代表标签页的 UI 组件。
     */
    Node getNode();

    /**
     * 获取与标签页相关的功能。
     *
     * @return 返回一个 Fun 对象，代表标签页的功能。
     */
    Fun getFun();

    /**
     * 左键点击操作。
     * 当用户左键点击标签页时执行该方法。
     */
    void leftClickAction();

    /**
     * 右键点击操作。
     * 当用户右键点击标签页时执行该方法。
     */
    void rightClickAction();

    /**
     * 设置标签页的背景颜色。
     *
     * @param color 颜色值，通常为十六进制字符串（如 "#FFFFFF"）或颜色名称。
     */
    void setBackground(String color);
}
