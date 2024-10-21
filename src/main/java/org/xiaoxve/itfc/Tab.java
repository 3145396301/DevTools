package org.xiaoxve.itfc;

import javafx.scene.Node;

public interface Tab {
    Node getNode();

    Fun getFun();

    void leftClickAction();
    void rightClickAction();

    //设置背景颜色
    void setBackground(String color);
}
