package org.xiaoxve.fun;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import lombok.Data;
import org.xiaoxve.component.LeftTab;
import org.xiaoxve.itfc.Tab;
@Data
public abstract class BaseTab implements Tab {
    private String name;
    private String icon;
    private HBox node;
    private LeftTab leftTab;
    private ImageView imageView;

    public BaseTab(String name, String icon, LeftTab leftTab) {
        this.name = name;
        this.icon = icon;
        this.leftTab = leftTab;
        init();
    }
    public void init() {
        // 加载图标资源
        Image icon = new Image(getClass().getResourceAsStream(this.icon));
        imageView = new ImageView(icon);
        // 创建 HBox 并设置属性
        node = new HBox();
        Text text = new Text(this.name);
        text.setFont(Font.font(15)); // 设置文本字体大小
        // 设置 HBox 的对齐方式为居中
        node.setAlignment(javafx.geometry.Pos.CENTER);
        // 添加子节点到 HBox
        node.getChildren().addAll(imageView, text);
        // 使用 CSS 来控制布局
        node.setStyle("-fx-alignment: center; -fx-padding: 5px;-fx-border-color: #000000;-fx-border-radius: 5%;-fx-background-radius: 12%;-fx-background-color: #68686c;"); // 设置内边距
        imageView.setStyle("-fx-max-width: 50%; -fx-min-width: 50%;"); // 设置 ImageView 最大最小宽度为50%
        text.setStyle("-fx-wrap-text: true; -fx-max-width: 50%; -fx-min-width: 50%;"); // 设置 Text 最大最小宽度为50%
        adaptiveWidthHeight();
        node.setOnMouseClicked(event -> {
            if (event.getButton().equals(javafx.scene.input.MouseButton.PRIMARY))
            // 左键单击事件处理
            leftClickAction();
            if (event.getButton().equals(javafx.scene.input.MouseButton.SECONDARY))
            // 右键单击事件处理
            rightClickAction();
        });
    }

    public void adaptiveWidthHeight(){
        node.setPrefWidth(leftTab.getWidth());
        // 保持图标比例
        imageView.setPreserveRatio(true);
        // 重新计算 ImageView 的宽度，使其适应新的宽度
        imageView.setFitWidth(leftTab.getWidth() * 0.2);
    }
    @Override
    public void leftClickAction() {
        leftTab.switchTab(this);
    }

    @Override
    public void rightClickAction() {
        throw new RuntimeException("未实施右键单击操作.");
    }
    @Override
    //设置背景颜色
    public void setBackground(String color){
        String style = node.getStyle();
        //使用正则匹配替换样式
        node.setStyle(style.replaceAll("-fx-background-color: .*;", "-fx-background-color: " + color + ";"));
    }

}
