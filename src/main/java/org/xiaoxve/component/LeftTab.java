package org.xiaoxve.component;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Data;
import org.xiaoxve.itfc.Tab;

import java.awt.*;
import java.util.List;

/**
 * LeftTab 类表示左侧标签页的组件，
 * 负责管理和显示标签页。
 */
@Data
public class LeftTab {
    private RightFunction rightFunction; // 右侧功能区的引用
    private List<Tab> tabs; // 标签页列表
    private HBox root; // 根布局
    private VBox hBox; // 垂直布局容器，用于显示标签页
    private Tab currentTab; // 当前选中的标签页
    private int currentTabIndex; // 当前标签页的索引
    private boolean isInit; // 是否已初始化

    /**
     * 构造函数，初始化 LeftTab 对象。
     *
     * @param root 根布局
     */
    public LeftTab(HBox root) {
        this.root = root; // 设置根布局
        hBox = new VBox(); // 初始化垂直布局
        System.out.println("root" + root.getPrefWidth()); // 输出根布局的宽度
        hBox.setPrefWidth(root.getPrefWidth() * 0.2); // 设置 hBox 的宽度为根布局的 20%
        hBox.setPrefHeight(root.getPrefHeight()); // 设置 hBox 的高度与根布局相同
        hBox.setSpacing(10); // 设置子节点间的间距
    }

    /**
     * 初始化标签页，并将其添加到布局中。
     *
     * @param tabs 标签页列表
     */
    public void init(List<Tab> tabs) {
        this.tabs = tabs; // 设置标签页列表
        root.getChildren().add(hBox); // 将 hBox 添加到根布局中
        addPrivateTab(); // 添加标签页
        adaptiveWidthHeight(); // 调整宽高
    }

    /**
     * 添加单个标签页到垂直布局中。
     *
     * @param tab 标签页
     */
    private void addChild(Tab tab) {
        hBox.getChildren().add(tab.getNode()); // 将标签页的节点添加到 hBox
    }

    /**
     * 添加多个标签页到垂直布局中。
     *
     * @param tabs 标签页列表
     */
    private void addAllChild(List<Tab> tabs) {
        tabs.forEach(this::addChild); // 遍历标签页并添加
    }

    /**
     * 添加私有标签页到布局中。
     */
    private void addPrivateTab() {
        addAllChild(tabs); // 添加所有标签页
    }

    /**
     * 自适应调整宽高。
     * 根据根布局的大小调整 hBox 的宽高。
     */
    public void adaptiveWidthHeight() {
        hBox.setPrefWidth(root.getPrefWidth() * 0.2); // 设置 hBox 的宽度为根布局的 20%
        hBox.setPrefHeight(root.getPrefHeight()); // 设置 hBox 的高度与根布局相同
    }

    /**
     * 切换当前标签页。
     *
     * @param tab 要切换到的标签页
     */
    public void switchTab(Tab tab) {
        if (tab == currentTab) {
            return; // 如果已经是当前标签页，则不进行切换
        }
        if (currentTab != null) {
            currentTab.setBackground("#F0FADB"); // 还原之前标签页的背景颜色
        }
        currentTab = tab; // 设置当前标签页
        Color color = new Color(0xBAF0FF);
        tab.setBackground("#BAF0FF"); // 设置新标签页的背景颜色
        currentTabIndex = tabs.indexOf(tab); // 获取当前标签页的索引
        rightFunction.switchFun(tab.getFun()); // 切换右侧功能区的功能
    }

    /**
     * 获取 hBox 的宽度。
     *
     * @return hBox 的宽度
     */
    public double getWidth() {
        return hBox.getPrefWidth(); // 返回 hBox 的宽度
    }
}
