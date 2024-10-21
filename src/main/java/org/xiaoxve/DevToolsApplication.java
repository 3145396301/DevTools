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

public class DevToolsApplication extends Application {
    @Getter
    private static HBox root;
    @Getter
    private static LeftTab leftTab;
    @Getter
    private static RightFunction rightFunction;
    @Getter
    public static Stage mainStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage = primaryStage;
        primaryStage.setTitle("DevTools");
        root = new HBox();
        root.setId("root");
        root.setStyle("-fx-padding: 5px");
        setWindowSize(800,800);
        primaryStage.widthProperty().addListener((observable, oldValue, newValue)->{
            adaptiveWidth(newValue.doubleValue());
        });
        primaryStage.heightProperty().addListener((observable, oldValue, newValue)->{
            adaptiveHeight(newValue.doubleValue());
        });
        primaryStage.setScene(new Scene(root));
        LeftTab leftTab = new LeftTab(root);
        DevToolsApplication.leftTab = leftTab;
        List<Tab> tabs = getTabs(leftTab);
        leftTab.init(tabs);
        RightFunction rightFunction = new RightFunction(root);
        DevToolsApplication.rightFunction = rightFunction;
        leftTab.setRightFunction(rightFunction);
        primaryStage.show();
    }

    public static List<Tab> getTabs(LeftTab leftTab)
    {
        List<Tab> tabs = new ArrayList<>();
        InputStream resourceAsStream = DevToolsApplication.class.getResourceAsStream("/conf.json");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Conf conf = objectMapper.readValue(resourceAsStream, Conf.class);
            for (ConfTab confTab : conf.getTabs()) {
                if (confTab.getEnabled() != null && confTab.getEnabled())
                tabs.add(confTab.generateInstance(leftTab));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tabs;
    }

    public void adaptiveWidth(double width){
        root.setPrefWidth(width);
        leftTab.adaptiveWidthHeight();
        rightFunction.adaptiveWidthHeight();
    }
    public void adaptiveHeight(double height){
        root.setPrefHeight(height);
        leftTab.adaptiveWidthHeight();
        rightFunction.adaptiveWidthHeight();
    }

    public static void setWindowSize(double width, double height) {
        mainStage.setWidth(width);
        mainStage.setHeight(height);
        root.setPrefSize(width, height);
    }
}
