package org.xiaoxve.component;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.xiaoxve.itfc.Fun;

public class RightFunction {
    private HBox root;
    private Pane pane;
    private Fun currentFun;
    private boolean isInit = false;
    public RightFunction(HBox root) {
        this.root = root;
        this.pane = new Pane();
        init();
    }

    public void init() {
        if(isInit) return;
        root.getChildren().add(pane);
        adaptiveWidthHeight();
        isInit = true;
    }
    public void adaptiveWidthHeight() {
        pane.setPrefWidth(root.getPrefWidth()*0.8);
        pane.setPrefHeight(root.getPrefHeight());
    }

    public void switchFun(Fun fun) {
        if (fun == currentFun){
            return;
        }
        currentFun = fun;
        pane.getChildren().clear();
        fun.adaptiveWidthHeight(pane);
        pane.getChildren().add(fun.getNode());
    }
}
