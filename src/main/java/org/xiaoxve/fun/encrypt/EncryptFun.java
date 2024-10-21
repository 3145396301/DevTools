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

/**
 * EncryptFun 类实现了加密功能，
 * 提供 MD5、SHA-1、SHA-256、SHA-512 和 Bcrypt 的加密方法。
 */
public class EncryptFun implements Fun {
    private GridPane node; // 布局节点
    private TextField source; // 输入源文本的文本框
    private TextField md5; // MD5 加密结果文本框
    private TextField sha1; // SHA-1 加密结果文本框
    private TextField sha256; // SHA-256 加密结果文本框
    private TextField sha512; // SHA-512 加密结果文本框
    private TextField bcrypt; // Bcrypt 加密结果文本框

    /**
     * 构造函数，初始化布局和组件。
     */
    public EncryptFun() {
        node = new GridPane(); // 初始化网格布局
        node.setHgap(10); // 设置水平间距
        node.setVgap(10); // 设置垂直间距

        // 初始化文本框和标签
        source = new TextField();
        Label label = new Label("源文本");
        md5 = new TextField();
        md5.setEditable(false); // MD5 文本框不可编辑
        Label label1 = new Label("MD5");
        sha1 = new TextField();
        sha1.setEditable(false); // SHA-1 文本框不可编辑
        Label label2 = new Label("SHA-1");
        sha256 = new TextField();
        sha256.setEditable(false); // SHA-256 文本框不可编辑
        Label label3 = new Label("SHA-256");
        sha512 = new TextField();
        sha512.setEditable(false); // SHA-512 文本框不可编辑
        Label label4 = new Label("SHA-512");
        bcrypt = new TextField();
        bcrypt.setEditable(false); // Bcrypt 文本框不可编辑
        Label label5 = new Label("Bcrypt");

        // 将标签和文本框添加到网格布局
        node.addRow(0, label, source);
        node.addRow(1, label1, md5);
        node.addRow(2, label2, sha1);
        node.addRow(3, label3, sha256);
        node.addRow(4, label4, sha512);
        node.addRow(5, label5, bcrypt);

        // 监听源文本框的文本变化，实时更新加密结果
        source.textProperty().addListener((observable, oldValue, newValue) -> {
            md5.setText(md5(newValue));
            sha1.setText(sha1(newValue));
            sha256.setText(sha256(newValue));
            sha512.setText(sha512(newValue));
            bcrypt.setText(bcrypt(newValue));
        });
    }

    // MD5 加密
    public String md5(String str) {
        return hashWithAlgorithm(str, "MD5");
    }

    // SHA-1 加密
    public String sha1(String str) {
        return hashWithAlgorithm(str, "SHA-1");
    }

    // SHA-256 加密
    public String sha256(String str) {
        return hashWithAlgorithm(str, "SHA-256");
    }

    // SHA-512 加密
    public String sha512(String str) {
        return hashWithAlgorithm(str, "SHA-512");
    }

    // Bcrypt 加密
    public String bcrypt(String str) {
        return BCrypt.hashpw(str, BCrypt.gensalt()); // 使用 Bcrypt 加密
    }

    // 使用指定算法进行哈希加密
    private String hashWithAlgorithm(String str, String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm); // 获取消息摘要实例
            byte[] hash = digest.digest(str.getBytes()); // 计算哈希值
            StringBuilder hexString = new StringBuilder(); // 用于存储十六进制字符串
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b); // 转换为十六进制
                if (hex.length() == 1) hexString.append('0'); // 确保每个字节都是两位数
                hexString.append(hex); // 添加到结果字符串
            }
            return hexString.toString(); // 返回十六进制字符串
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e); // 抛出运行时异常
        }
    }

    @Override
    public Node getNode() {
        return node; // 返回布局节点
    }

    @Override
    public void adaptiveWidthHeight(Pane parent) {
        // 设置节点位置以实现自适应布局
        node.setLayoutX(parent.getPrefWidth() / 4);
        node.setLayoutY(parent.getPrefHeight() / 4);
    }
}
