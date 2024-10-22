package org.xiaoxve.fun.ssl;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import org.xiaoxve.DevToolsApplication;
import org.xiaoxve.itfc.Fun;

import java.io.File;
import java.io.IOException;

public class SSLFun implements Fun {
    private GridPane node; // 使用GridPane进行布局
    private TextField commonNameField;
    private TextField timeField;
    private TextField savePathField;
    private CheckBox encryptCheckBox;
    private PasswordField passwordField;
    private Button generateCertButton;
    private Button convertFormatButton;
    private TextArea outputArea;

    public SSLFun() {
        node = new GridPane();
        node.setPadding(new Insets(20));
        node.setVgap(15);
        node.setHgap(15);
        node.setStyle("-fx-background-color: #939398;"); // 与EncryptFun一致

        commonNameField = createStyledTextField("请输入通用名称(CN)");
        timeField = createStyledTextField("请输入有效时间（天数）");
        savePathField = createStyledTextField("请选择保存路径");
        savePathField.setEditable(false);
        savePathField.setOnMouseClicked(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("选择目录");
            File file = directoryChooser.showDialog(DevToolsApplication.getMainStage());
            if (file != null) {
                savePathField.setText(file.getAbsolutePath());
            }
        });

        encryptCheckBox = new CheckBox("加密证书");
        encryptCheckBox.setStyle("-fx-font-size: 14px;");

        Label passwordLabel = new Label("密码:");
        passwordLabel.setVisible(false);
        passwordLabel.setTextFill(Color.DARKGRAY);
        passwordLabel.setFont(Font.font("Arial", 16));
        passwordField = new PasswordField();
        passwordField.setPromptText("请输入密码");
        passwordField.setVisible(false);

        encryptCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            passwordField.setVisible(newValue);
            passwordLabel.setVisible(newValue);
        });

        generateCertButton = createStyledButton("生成自签名证书", "#4CAF50");
        convertFormatButton = createStyledButton("转换证书格式", "#008CBA");

        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPrefHeight(100);
        outputArea.setStyle("-fx-background-color: white; -fx-font-size: 14px;");

        generateCertButton.setOnAction(event -> generateSelfSignedCert());
        convertFormatButton.setOnAction(event -> convertCertFormat());

        // 将控件添加到GridPane中
        node.add(new Label("通用名称:"), 0, 0);
        node.add(commonNameField, 1, 0);
        node.add(new Label("有效时间(天):"), 0, 1);
        node.add(timeField, 1, 1);
        node.add(new Label("保存路径:"), 0, 2);
        node.add(savePathField, 1, 2);
        node.add(encryptCheckBox, 0, 3, 2, 1); // 占据两列
        node.add(passwordLabel, 0, 4);
        node.add(passwordField, 1, 4);
        node.add(generateCertButton, 0, 5);
        node.add(convertFormatButton, 1, 5);
        node.add(outputArea, 0, 6, 2, 1); // 占据两列
    }

    private TextField createStyledTextField(String prompt) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        textField.setStyle("-fx-border-color: #535151; -fx-background-color: white; -fx-font-size: 14px; -fx-padding: 5;");
        return textField;
    }

    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 14px;");
        return button;
    }

    private void generateSelfSignedCert() {
        String cn = commonNameField.getText();
        String days = timeField.getText();
        String path = savePathField.getText();
        boolean isEncrypted = encryptCheckBox.isSelected();
        String password = passwordField.getText();

        if (cn.isEmpty() || days.isEmpty() || path.isEmpty()) {
            showAlert("错误", "请填写通用名称、有效时间和保存路径");
            return;
        }

        try {
            String cmd = "openssl req -newkey rsa:2048 -nodes -keyout " + path + "/server.key -x509 -days " + days + " -out " + path + "/server.crt -subj \"/CN=" + cn + "\"";
            if (isEncrypted && !password.isEmpty()) {
                cmd += " -passout pass:" + password;
            }

            Process process = Runtime.getRuntime().exec(cmd.split(" "));
            process.waitFor();
            showAlert("成功", "证书已成功生成!");
        } catch (IOException | InterruptedException e) {
            showAlert("失败", "生成证书时发生错误: " + e.getMessage());
        }
    }

    private void convertCertFormat() {
        showAlert("提示", "证书格式转换功能尚未实现！");
    }

    private void showAlert(String title, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    @Override
    public Node getNode() {
        return node;
    }

    @Override
    public void adaptiveWidthHeight(Pane parent) {
        node.setPrefWidth(500);
        node.setPrefHeight(500);
        double x = (parent.getWidth() - node.getPrefWidth()) / 2;
        double y = (parent.getHeight() - node.getPrefHeight()) / 2;
        node.setLayoutX(x);
        node.setLayoutY(y);
    }
}
