package org.xiaoxve.fun.ssl;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import lombok.Data;
import org.xiaoxve.DevToolsApplication;
import org.xiaoxve.component.LeftTab;
import org.xiaoxve.fun.BaseTab;
import org.xiaoxve.itfc.Fun;


import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class SSLTab extends BaseTab {
    private SSLFun sslFun;
    public SSLTab(String name, String icon, LeftTab leftTab) {
        super(name, icon, leftTab);
        sslFun = new SSLFun();
    }

    @Override
    public Fun getFun() {
        return sslFun;
    }

    private void showInstallDialog(Stage ownerStage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("安装OpenSSL");
        alert.setHeaderText(null);
        alert.setContentText("OpenSSL未安装，是否前往安装？");

        ButtonType yesButton = new ButtonType("前往安装");
        ButtonType noButton = new ButtonType("取消");

        alert.getButtonTypes().setAll(yesButton, noButton);

        // 设置样式
        alert.getDialogPane().setStyle("-fx-background-color: #F0F0F0; -fx-font-family: 'Arial'; -fx-font-size: 14px;");

        alert.initOwner(ownerStage);
        alert.setResizable(false);

        alert.showAndWait().ifPresent(response -> {
            if (response == yesButton) {
                installOpenSSL();
            }
        });
    }

    @Override
    public void leftClickAction() {
        if (!isOpenSSLInstalled()) {
            showInstallDialog(DevToolsApplication.mainStage);
        } else {
            super.leftClickAction();
        }
    }


    public boolean isOpenSSLInstalled() {
        try {
            Process process = new ProcessBuilder("cmd.exe", "/c", "openssl version").start();
            return process.waitFor() == 0;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }
    public void installOpenSSL() {
        // 浏览器打开https://slproweb.com/download/Win64OpenSSL-3_3_2.exe
        try {
            Desktop.getDesktop().browse(new URI("https://slproweb.com/download/Win64OpenSSL-3_3_2.exe"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
