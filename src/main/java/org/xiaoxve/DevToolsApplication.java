package org.xiaoxve;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.Getter;
import org.xiaoxve.component.LeftTab;
import org.xiaoxve.component.RightFunction;
import org.xiaoxve.conf.Conf;
import org.xiaoxve.conf.ConfTab;
import org.xiaoxve.itfc.Tab;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * DevToolsApplication 是一个 JavaFX 应用程序的主类，
 * 用于创建开发工具的用户界面。
 */
public class DevToolsApplication extends Application {

    @Getter
    private static HBox root; // 主布局容器
    @Getter
    private static LeftTab leftTab; // 左侧标签页
    @Getter
    private static RightFunction rightFunction; // 右侧功能区
    @Getter
    public static Stage mainStage; // 主窗口

    /**
     * 应用程序的入口方法，用于初始化并显示主界面。
     *
     * @param primaryStage 主窗口
     * @throws Exception 可能抛出的异常
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage = primaryStage; // 设置主窗口
        primaryStage.setTitle("DevTools"); // 设置窗口标题

        root = new HBox(); // 初始化主布局为 HBox
        root.setId("root"); // 设置 ID
        root.setStyle("-fx-padding: 5px"); // 设置内边距

        setWindowSize(800, 800); // 设置窗口初始大小

        // 监听窗口宽度变化
        primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> {
            adaptiveWidth(newValue.doubleValue()); // 调整宽度
        });

        // 监听窗口高度变化
        primaryStage.heightProperty().addListener((observable, oldValue, newValue) -> {
            adaptiveHeight(newValue.doubleValue()); // 调整高度
        });

        primaryStage.setScene(new Scene(root)); // 设置场景
        LeftTab leftTab = new LeftTab(root); // 创建左侧标签页
        DevToolsApplication.leftTab = leftTab; // 设置静态引用

        List<Tab> tabs = getTabs(leftTab); // 获取标签页内容
        leftTab.init(tabs); // 初始化左侧标签页

        RightFunction rightFunction = new RightFunction(root); // 创建右侧功能区
        DevToolsApplication.rightFunction = rightFunction; // 设置静态引用

        leftTab.setRightFunction(rightFunction); // 关联左侧标签与右侧功能区
        primaryStage.show(); // 显示主窗口
    }

    /**
     * 获取标签页列表
     *
     * @param leftTab 左侧标签页
     * @return 标签页列表
     */
    public static List<Tab> getTabs(LeftTab leftTab) {
        List<Tab> tabs = new ArrayList<>(); // 存储标签页的列表
        InputStream resourceAsStream = DevToolsApplication.class.getResourceAsStream("/conf.json"); // 获取配置文件
        ObjectMapper objectMapper = new ObjectMapper(); // 创建对象映射器

        try {
            Conf conf = objectMapper.readValue(resourceAsStream, Conf.class); // 读取配置文件
            for (ConfTab confTab : conf.getTabs()) { // 遍历每个标签配置
                if (confTab.getEnabled() != null && confTab.getEnabled()) // 检查标签是否启用
                    tabs.add(confTab.generateInstance(leftTab)); // 生成实例并添加到列表
            }
        } catch (Exception e) {
            e.printStackTrace(); // 输出异常堆栈
        }
        return tabs; // 返回标签页列表
    }

    /**
     * 自适应宽度
     *
     * @param width 新宽度
     */
    public void adaptiveWidth(double width) {
        root.setPrefWidth(width); // 设置根布局的宽度
        leftTab.adaptiveWidthHeight(); // 调整左侧标签的尺寸
        rightFunction.adaptiveWidthHeight(); // 调整右侧功能区的尺寸
    }

    /**
     * 自适应高度
     *
     * @param height 新高度
     */
    public void adaptiveHeight(double height) {
        root.setPrefHeight(height); // 设置根布局的高度
        leftTab.adaptiveWidthHeight(); // 调整左侧标签的尺寸
        rightFunction.adaptiveWidthHeight(); // 调整右侧功能区的尺寸
    }

    /**
     * 设置窗口大小
     *
     * @param width  宽度
     * @param height 高度
     */
    public static void setWindowSize(double width, double height) {
        mainStage.setWidth(width); // 设置主窗口宽度
        mainStage.setHeight(height); // 设置主窗口高度
        root.setPrefSize(width, height); // 设置根布局的尺寸
    }
}
