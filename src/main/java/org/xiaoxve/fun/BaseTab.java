package org.xiaoxve.fun;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import lombok.Data;
import org.xiaoxve.component.LeftTab;
import org.xiaoxve.itfc.Tab;

/**
 * BaseTab 抽象类实现了 Tab 接口的基本功能，
 * 提供标签页的通用属性和行为，供具体标签页类继承。
 */
@Data
public abstract class BaseTab implements Tab {
    private String name; // 标签页名称
    private String icon; // 标签页图标路径
    private HBox node; // 标签页的 UI 节点
    private LeftTab leftTab; // 关联的左侧标签
    private ImageView imageView; // 图标的 ImageView

    /**
     * 构造函数，初始化标签页属性。
     *
     * @param name 标签页名称
     * @param icon 标签页图标路径
     * @param leftTab 关联的左侧标签
     */
    public BaseTab(String name, String icon, LeftTab leftTab) {
        this.name = name;
        this.icon = icon;
        this.leftTab = leftTab;
        init(); // 调用初始化方法
    }

    /**
     * 初始化标签页的 UI 组件。
     */
    public void init() {
        // 加载图标资源
        Image icon = new Image(getClass().getResourceAsStream(this.icon));
        imageView = new ImageView(icon); // 创建图标的 ImageView
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(100); // 设置固定宽度（根据需求调整）

        // 创建 HBox 并设置属性
        node = new HBox();

        // 创建左侧 VBox 来放置图标
        VBox leftBox = new VBox();
        leftBox.getChildren().add(imageView);
        leftBox.setPrefWidth(100);
        leftBox.setAlignment(javafx.geometry.Pos.CENTER);

        // 创建右侧 VBox 来放置名称
        VBox rightBox = new VBox();
        Text text = new Text(this.name); // 创建文本显示标签页名称
        text.setFont(Font.font(15)); // 设置文本字体大小
        rightBox.getChildren().add(text);
        rightBox.setPrefWidth(100);
        rightBox.setAlignment(Pos.CENTER_LEFT); // 左对齐

        // 将左右两个 VBox 添加到 HBox
        node.getChildren().addAll(leftBox, rightBox);

        // 设置 HBox 的对齐方式为居中
        node.setAlignment(javafx.geometry.Pos.CENTER);
        node.setPrefHeight(50); // 设置 HBox 的高度（根据需求调整）

        // 使用 CSS 来控制布局
        node.setStyle("-fx-padding: 5px;" +
                "-fx-border-color: #000000; -fx-border-radius: 5%;" +
                "-fx-background-radius: 12%; -fx-background-color: #F0FADB;");

        adaptiveWidthHeight(); // 调整组件的宽高

        // 设置鼠标点击事件处理
        node.setOnMouseClicked(event -> {
            if (event.getButton().equals(javafx.scene.input.MouseButton.PRIMARY)) {
                // 左键单击事件处理
                leftClickAction();
            }
            if (event.getButton().equals(javafx.scene.input.MouseButton.SECONDARY)) {
                // 右键单击事件处理
                rightClickAction();
            }
        });
    }


    /**
     * 自适应宽高调整方法。
     * 根据左侧标签的宽度调整当前标签页的组件尺寸。
     */
    public void adaptiveWidthHeight() {
        node.setPrefWidth(leftTab.getWidth()); // 设置 HBox 的宽度

        // 保持图标比例
        imageView.setPreserveRatio(true);
        // 重新计算 ImageView 的宽度，使其适应新的宽度
        imageView.setFitWidth(leftTab.getWidth() * 0.2);
    }

    @Override
    public void leftClickAction() {
        leftTab.switchTab(this); // 切换到当前标签页
    }

    @Override
    public void rightClickAction() {
        throw new RuntimeException("未实施右键单击操作."); // 抛出未实现异常
    }


    /**
     * 设置背景颜色
     *
     * @param color 颜色值，通常为十六进制字符串（如 "#FFFFFF"）或颜色名称。
     */
    @Override
    public void setBackground(String color) {
        String style = node.getStyle(); // 获取当前样式
        // 使用正则匹配替换样式
        node.setStyle(style.replaceAll("-fx-background-color: .*;", "-fx-background-color: " + color + ";"));
    }
}
