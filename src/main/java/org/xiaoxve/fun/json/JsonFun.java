package org.xiaoxve.fun.json;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.xiaoxve.itfc.Fun;

import javafx.event.ActionEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonFun implements Fun {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static TextArea jsonTextArea;
    private TextField classNameField;
    private VBox vbox;
    private HBox buttonHBox;
    private Button formatButton;
    private Button generateEntityButton;
    private ToggleGroup nameConventionGroup;
    private RadioButton camelCaseButton;
    private RadioButton underscoreCaseButton;

    public JsonFun() {
        jsonTextArea = new TextArea();
        jsonTextArea.setWrapText(true);
        jsonTextArea.setPrefSize(300, 200); // 设置文本框的首选宽度和高度

        classNameField = new TextField();
        classNameField.setPromptText("请输入类名");

        formatButton = new Button("格式化");
        formatButton.setTooltip(new Tooltip("格式化 JSON 代码"));

        generateEntityButton = new Button("生成实体类");
        generateEntityButton.setTooltip(new Tooltip("根据 JSON 生成 Java 实体类"));

        // 使用 HBox 来容纳按钮和文本框
        buttonHBox = new HBox(formatButton, generateEntityButton, classNameField);
        buttonHBox.setSpacing(5); // 设置控件间的间距
        buttonHBox.setAlignment(Pos.CENTER); // 设置对齐方式

        // 添加命名规范选择
        nameConventionGroup = new ToggleGroup();
        camelCaseButton = new RadioButton("驼峰命名");
        underscoreCaseButton = new RadioButton("下划线大小写");

        camelCaseButton.setToggleGroup(nameConventionGroup);
        underscoreCaseButton.setToggleGroup(nameConventionGroup);

        camelCaseButton.setSelected(true); // 默认选择 Camel Case

        // 使用 HBox 来容纳命名规范选择
        HBox nameConventionHBox = new HBox(camelCaseButton, underscoreCaseButton);
        nameConventionHBox.setSpacing(5);
        nameConventionHBox.setAlignment(Pos.CENTER);

        // 使用 VBox 进行垂直布局
        vbox = new VBox(buttonHBox, nameConventionHBox, jsonTextArea);
        vbox.setSpacing(10); // 设置控件间的间距

        // 设置按钮点击事件处理器
        setupButtonHandlers();

        // 添加命名规范选择变化时的监听器
        nameConventionGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                boolean isCamelCase = newValue == camelCaseButton;
                String json = jsonTextArea.getText();
                String className = classNameField.getText();
                if (className.isEmpty()) {
                    className = "GeneratedEntity"; // 默认类名
                }
                generateEntityClass(json, className, isCamelCase);
            }
        });
    }

    private static void generateEntityClass(String jsonString, String className, boolean isCamelCase) {
        // 解析 JSON 字符串
        try {
            if (jsonString.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(jsonString);
                generateEntityFromObject(jsonObject, className, isCamelCase);
            } else if (jsonString.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(jsonString);
                generateEntityFromArray(jsonArray, className, isCamelCase);
            } else {
                throw new IllegalArgumentException("Invalid JSON format");
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("JSON 解析错误");
            alert.setHeaderText("提供的 JSON 数据格式不正确，请检查后重试！");
            alert.setContentText(e.getMessage());
            alert.showAndWait(); // 显示对话框
            System.err.println("解析 JSON 失败: " + e.getMessage());
        }
    }

    private static void generateEntityFromObject(JSONObject jsonObject, String className, boolean isCamelCase) {
        // 生成类定义
        StringBuilder classDefinition = new StringBuilder();
        classDefinition.append("public class ").append(className).append(" {\n");

        // 遍历 JSON 对象中的键值对
        for (String key : jsonObject.keySet()) {
            String fieldName = isCamelCase ? toCamelCase(key) : key.toLowerCase().replace('_', ' ');

            Object value = jsonObject.get(key);

            // 确定字段类型
            String fieldType = determineType(value);

            // 添加字段定义
            classDefinition.append("\tprivate ").append(fieldType).append(" ").append(fieldName).append(";\n");

            // 添加 getter 方法
            classDefinition.append("\tpublic ").append(fieldType).append(" get").append(capitalizeFirstLetter(fieldName)).append("() { return ").append(fieldName).append("; }\n");

            // 添加 setter 方法
            classDefinition.append("\tpublic void set").append(capitalizeFirstLetter(fieldName)).append("(").append(fieldType).append(" ").append(fieldName).append(") { this.").append(fieldName).append(" = ").append(fieldName).append("; }\n");
        }

        // 结束类定义
        classDefinition.append("}");

        // 设置文本框的内容为生成的类定义
        jsonTextArea.setText(classDefinition.toString());
    }

    private static void generateEntityFromArray(JSONArray jsonArray, String className, boolean isCamelCase) {
        if (jsonArray.size() > 0) {
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            generateEntityFromObject(jsonObject, className, isCamelCase);
        } else {
            System.err.println("JSON 数组为空");
        }
    }

    private static String capitalizeFirstLetter(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    private static String toCamelCase(String s) {
        // 检查字符串是否包含下划线，如果包含，则进行转换
        if (s.contains("_")) {
            // 使用 Pattern 和 Matcher 替换下划线
            Pattern pattern = Pattern.compile("_(.)");
            Matcher matcher = pattern.matcher(s);
            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                // 将匹配到的字符转换为大写
                matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
            }
            matcher.appendTail(sb);
            return sb.toString();
        } else {
            // 不包含下划线，直接返回原字符串
            return s;
        }
    }

    private static String determineType(Object value) {
        if (value instanceof String) {
            return "String";
        } else if (value instanceof Integer) {
            return "int";
        } else if (value instanceof Boolean) {
            return "boolean";
        } else if (value instanceof JSONArray) {
            return "List<String>"; // 假设数组元素都是字符串
        } else if (value instanceof JSONObject) {
            return "Object"; // 可以递归生成子类
        }
        return "Object";
    }

    private void setupButtonHandlers() {
        formatButton.setOnAction(this::onFormatButtonClick);
        generateEntityButton.setOnAction(this::onGenerateEntityButtonClick);
    }

    private void onFormatButtonClick(ActionEvent event) {
        String json = jsonTextArea.getText();
        String formattedJson = format(json);
        jsonTextArea.setText(formattedJson);
    }

    private void onGenerateEntityButtonClick(ActionEvent event) {
        String json = jsonTextArea.getText();
        String className = classNameField.getText();
        if (className.isEmpty()) {
            className = "GeneratedEntity"; // 默认类名
        }
        Toggle selectedToggle = nameConventionGroup.getSelectedToggle();
        boolean isCamelCase = selectedToggle == camelCaseButton;
        generateEntityClass(json, className, isCamelCase);
    }

    @Override
    public Node getNode() {
        return vbox;
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

            jsonTextArea.setPrefWidth(prefWidth); // 设置为父容器宽度
            jsonTextArea.setPrefHeight(prefHeight * 0.5); // 设置为父容器高度的一半

            // 调整 VBox 的首选宽度和高度
            vbox.setPrefWidth(prefWidth);
            vbox.setPrefHeight(prefHeight);
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