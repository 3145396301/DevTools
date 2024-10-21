package org.xiaoxve.component;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.xiaoxve.itfc.Fun;

/**
 * RightFunction 类用于管理右侧功能区的组件，
 * 负责显示当前功能并自适应调整其大小。
 */
public class RightFunction {
    private HBox root; // 根布局
    private Pane pane; // 功能区的面板
    private Fun currentFun; // 当前功能
    private boolean isInit = false; // 是否已初始化

    /**
     * 构造函数，初始化 RightFunction 对象。
     *
     * @param root 根布局
     */
    public RightFunction(HBox root) {
        this.root = root; // 设置根布局
        this.pane = new Pane(); // 初始化面板
        init(); // 调用初始化方法
    }

    /**
     * 初始化功能区。
     * 仅在第一次调用时执行，防止重复初始化。
     */
    public void init() {
        if (isInit) return; // 如果已经初始化则返回
        root.getChildren().add(pane); // 将面板添加到根布局
        adaptiveWidthHeight(); // 调整面板宽高
        isInit = true; // 标记为已初始化
    }

    /**
     * 自适应调整宽高。
     * 根据根布局的大小调整面板的宽高。
     */
    public void adaptiveWidthHeight() {
        pane.setPrefWidth(root.getPrefWidth() * 0.8); // 设置面板宽度为根布局的 80%
        pane.setPrefHeight(root.getPrefHeight()); // 设置面板高度与根布局相同
    }

    /**
     * 切换当前功能。
     *
     * @param fun 要切换到的功能
     */
    public void switchFun(Fun fun) {
        if (fun == currentFun) {
            return; // 如果已经是当前功能，则不进行切换
        }
        currentFun = fun; // 设置当前功能
        pane.getChildren().clear(); // 清空面板中的子节点
        fun.adaptiveWidthHeight(pane); // 调整新功能的大小
        pane.getChildren().add(fun.getNode()); // 添加新功能的节点到面板
    }
}
