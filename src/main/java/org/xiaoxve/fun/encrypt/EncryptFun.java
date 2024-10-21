package org.xiaoxve.fun.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
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
        node.setHgap(10);
        node.setVgap(10);
        source = new TextField();
        Label label = new Label("源文本");
        md5 = new TextField();
        md5.setEditable(false);
        Label label1 = new Label("MD5");
        sha1 = new TextField();
        sha1.setEditable(false);
        Label label2 = new Label("SHA-1");
        sha256 = new TextField();
        sha256.setEditable(false);
        Label label3 = new Label("SHA-256");
        sha512 = new TextField();
        sha512.setEditable(false);
        Label label4 = new Label("SHA-512");
        bcrypt = new TextField();
        bcrypt.setEditable(false);
        Label label5 = new Label("Bcrypt");
        node.addRow(0, label, source);
        node.addRow(1, label1, md5);
        node.addRow(2, label2, sha1);
        node.addRow(3, label3, sha256);
        node.addRow(4, label4, sha512);
        node.addRow(5, label5, bcrypt);
        source.textProperty().addListener((observable, oldValue, newValue) -> {
            md5.setText(md5(newValue));
            sha1.setText(sha1(newValue));
            sha256.setText(sha256(newValue));
            sha512.setText(sha512(newValue));
            bcrypt.setText(bcrypt(newValue));
        });
    }


    // MD5加密
    public String md5(String str) {
        return hashWithAlgorithm(str, "MD5");
    }

    // SHA-1加密
    public String sha1(String str) {
        return hashWithAlgorithm(str, "SHA-1");
    }

    // SHA-256加密
    public String sha256(String str) {
        return hashWithAlgorithm(str, "SHA-256");
    }

    // SHA-512加密
    public String sha512(String str) {
        return hashWithAlgorithm(str, "SHA-512");
    }

    // Bcrypt加密
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
    public void adaptiveWidthHeight(Pane parent){
        node.setLayoutX(parent.getPrefWidth()/4);
        node.setLayoutY(parent.getPrefHeight()/4);
    }
}
