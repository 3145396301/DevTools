package org.xiaoxve.itfc;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * Fun 接口定义了与功能相关的基本行为和特性。
 * 实现该接口的类应提供具体的功能实现。
 */
public interface Fun {

    /**
     * 获取功能的 JavaFX 节点。
     *
     * @return 返回一个 Node 对象，代表功能的 UI 组件。
     */
    Node getNode();

    /**
     * 自适应宽高调整方法。
     * 根据父容器的大小调整功能组件的宽度和高度。
     *
     * @param parent 父容器，用于获取父容器的尺寸。
     */
    void adaptiveWidthHeight(Pane parent);
}
