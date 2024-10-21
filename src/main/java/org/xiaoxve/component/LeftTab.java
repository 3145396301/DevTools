package org.xiaoxve.component;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Data;
import org.xiaoxve.itfc.Tab;

import java.util.List;

@Data
public class LeftTab {
    RightFunction rightFunction;
    List<Tab> tabs;
    private HBox root;
    private VBox hBox;
    private Tab currentTab;
    private int currentTabIndex;
    private boolean isInit;
    public LeftTab( HBox root) {
        this.root = root;
        hBox = new VBox();
        System.out.println("root"+root.getPrefWidth());
        hBox.setPrefWidth(root.getPrefWidth()*0.2);
        hBox.setPrefHeight(root.getPrefHeight());
        hBox.setSpacing(10);
    }
    public void init(List<Tab> tabs) {
        this.tabs=tabs;
        root.getChildren().add(hBox);
        addPrivateTab();
        adaptiveWidthHeight();
    }
    private void addChild(Tab tab) {
        hBox.getChildren().add(tab.getNode());
    }
    private void addAllChild(List<Tab> tabs) {
        tabs.forEach(this::addChild);
    }
    private void addPrivateTab() {
        addAllChild(tabs);
    }
    public void adaptiveWidthHeight() {
        hBox.setPrefWidth(root.getPrefWidth()*0.2);
        hBox.setPrefHeight(root.getPrefHeight());
    }
    public void switchTab(Tab tab){
        if (tab==currentTab){
            return;
        }
        if (currentTab!=null){
            currentTab.setBackground("#68686c");
        }
        currentTab=tab;
        tab.setBackground("#F0FADB");
        currentTabIndex=tabs.indexOf(tab);
        rightFunction.switchFun(tab.getFun());
    }
    public double getWidth() {
        return hBox.getPrefWidth();
    }


}
