package org.xiaoxve.fun.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import org.xiaoxve.itfc.Fun;

public class JsonFun implements Fun {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private TextArea jsonTextArea;
    private Node node;

    public JsonFun() {
        jsonTextArea = new TextArea();
        jsonTextArea.setWrapText(true);
        node = jsonTextArea;
    }

    @Override
    public Node getNode() {
        return node;
    }

    @Override
    public void adaptiveWidthHeight(Pane parent) {
        if (parent == null) {
            System.err.println("Parent pane is not initialized");
            return;
        }

        if (jsonTextArea != null) {
            double prefWidth = parent.getPrefWidth();
            double prefHeight = parent.getPrefHeight();

            jsonTextArea.setPrefWidth(prefWidth);
            jsonTextArea.setPrefHeight(prefHeight);

            jsonTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
                Platform.runLater(() -> {
                    String json = newValue;
                    String formattedJson = format(json);
                    jsonTextArea.setText(formattedJson);
                });
            });
        } else {
            System.err.println("jsonTextArea is not initialized");
        }
    }

    private String format(String json) {
        if (json == null || json.trim().isEmpty()) {
            throw new IllegalArgumentException("JSON 字符串不能为空");
        }
        try {
            return GSON.toJson(GSON.fromJson(json, Object.class));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("JSON 格式化失败: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("JSON 格式化失败: " + e.getMessage(), e);
        }
    }
}
