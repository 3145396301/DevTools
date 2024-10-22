package org.xiaoxve.fun.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.mindrot.jbcrypt.BCrypt;
import org.xiaoxve.itfc.Fun;

public class EncryptFun implements Fun {
    private GridPane node;
    private TextField source;
    private TextField md5;
    private TextField sha1;
    private TextField sha256;
    private TextField sha512;
    private TextField bcrypt;

    public EncryptFun() {
        node = new GridPane();
        node.setHgap(15);
        node.setVgap(15);
        node.setStyle("-fx-background-color: #939398; -fx-padding: 20;");

        // Initialize text fields and labels with styles
        source = createStyledTextField();
        md5 = createStyledTextField();
        md5.setEditable(false);
        sha1 = createStyledTextField();
        sha1.setEditable(false);
        sha256 = createStyledTextField();
        sha256.setEditable(false);
        sha512 = createStyledTextField();
        sha512.setEditable(false);
        bcrypt = createStyledTextField();
        bcrypt.setEditable(false);

        // Initialize labels
        Label label = createStyledLabel("源文本");
        Label label1 = createStyledLabel("MD5");
        Label label2 = createStyledLabel("SHA-1");
        Label label3 = createStyledLabel("SHA-256");
        Label label4 = createStyledLabel("SHA-512");
        Label label5 = createStyledLabel("Bcrypt");

        // Add labels and text fields to the grid
        node.addRow(0, label, source);
        node.addRow(1, label1, md5);
        node.addRow(2, label2, sha1);
        node.addRow(3, label3, sha256);
        node.addRow(4, label4, sha512);
        node.addRow(5, label5, bcrypt);

        // Listen for text changes
        source.textProperty().addListener((observable, oldValue, newValue) -> {
            md5.setText(md5(newValue));
            sha1.setText(sha1(newValue));
            sha256.setText(sha256(newValue));
            sha512.setText(sha512(newValue));
            bcrypt.setText(bcrypt(newValue));
        });
    }

    private TextField createStyledTextField() {
        TextField textField = new TextField();
        textField.setStyle("-fx-border-color: #48484b; -fx-background-color: white; -fx-font-size: 14px; -fx-padding: 5;");
        return textField;
    }

    private Label createStyledLabel(String text) {
        Label label = new Label(text);
        label.setTextFill(Color.BLACK);
        label.setFont(Font.font("Arial", 16));
        return label;
    }

    public String md5(String str) {
        return hashWithAlgorithm(str, "MD5");
    }

    public String sha1(String str) {
        return hashWithAlgorithm(str, "SHA-1");
    }

    public String sha256(String str) {
        return hashWithAlgorithm(str, "SHA-256");
    }

    public String sha512(String str) {
        return hashWithAlgorithm(str, "SHA-512");
    }

    public String bcrypt(String str) {
        return BCrypt.hashpw(str, BCrypt.gensalt());
    }

    private String hashWithAlgorithm(String str, String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] hash = digest.digest(str.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Node getNode() {
        return node;
    }

    @Override
    public void adaptiveWidthHeight(Pane parent) {
        node.setLayoutX(parent.getPrefWidth() / 4);
        node.setLayoutY(parent.getPrefHeight() / 4);
    }
}
