package org.xiaoxve.fun.project;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.xiaoxve.DevToolsApplication;
import org.xiaoxve.itfc.Fun;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectToMdFun implements Fun {
    GridPane node;
    Label pathLabel;
    TextField pathTextField;
    VBox suffixEditArea;
    TextArea mdTextField;
    HBox hBox;
    String mode;
    List<TextField> fileSuffix;

    public ProjectToMdFun() {
        node = new GridPane();
        pathLabel = new Label("项目路径");
        pathTextField = new TextField();
        pathTextField.setPromptText("双击选择路径");
        suffixEditArea = new VBox();
        mdTextField = new TextArea();
        fileSuffix = new ArrayList<>();
        mode = "include";
        pathTextField.setEditable(false);
        mdTextField.setEditable(false);
        node.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        node.setHgap(10);
        node.setVgap(10);
        Label modeLabel = new Label("模式：");
        Button switchButton = new Button("包含");
        Button start = new Button("开始");
        start.setOnAction(event -> {
            String mdText = getMdText();
            mdTextField.setText(mdText);
        });
        Button addButton = new Button("+");
        addButton.setOnAction(event -> {
            TextField textField = new TextField();
            suffixEditArea.getChildren().add(textField);
            fileSuffix.add(textField);
        });
        hBox = new HBox(modeLabel, switchButton, start, addButton);
        hBox.setSpacing(10);
        suffixEditArea.getChildren().add(hBox);
        switchInclude();
        switchButton.setOnAction(event -> {
            if (mode.equals("include")) {
                switchButton.setText("排除");
                switchExclude();
            } else {
                switchButton.setText("包含");
                switchInclude();
            }
        });

        ScrollPane scrollPane = new ScrollPane(suffixEditArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        node.add(scrollPane, 0, 1, 1, 1);
        node.add(pathLabel, 0, 0);
        node.add(pathTextField, 1, 0);
        node.add(mdTextField, 1, 1);

        // 新增的组件
        Label fileNameLabel = new Label("文件名称：");
        TextField fileNameTextField = new TextField();
        fileNameTextField.setPromptText("输入文件名");
        Label filePathLabel = new Label("文件路径：");
        TextField filePathTextField = new TextField();
        filePathTextField.setPromptText("双击选择文件路径");
        filePathTextField.setEditable(false);
        filePathTextField.setOnMouseClicked(event -> {
            String path = DevToolsApplication.showDirectoryChooser();
            if (path != null) {
                filePathTextField.setText(path);
            }
        });

        Button saveButton = new Button("保存为文件");
        saveButton.setOnAction(event -> {
            String fileName = fileNameTextField.getText();
            String filePath = filePathTextField.getText();
            if (fileName.isEmpty() || filePath.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "文件名称和路径不能为空！");
                alert.showAndWait();
                return;
            }
            Path outputPath = Paths.get(filePath, fileName.endsWith(".md") ? fileName : fileName + ".md");
            try {
                Files.writeString(outputPath, mdTextField.getText());
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "文件保存成功：" + outputPath.toString());
                alert.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "文件保存失败！");
                alert.showAndWait();
            }
        });

        // 将新增的组件添加到布局中
        node.add(fileNameLabel, 0, 2);
        node.add(fileNameTextField, 1, 2);
        node.add(filePathLabel, 0, 3);
        node.add(filePathTextField, 1, 3);
        node.add(saveButton, 1, 4);

        node.setPadding(new Insets(0, 10, 10, 10));
        GridPane.setHgrow(mdTextField, Priority.ALWAYS);
        GridPane.setVgrow(mdTextField, Priority.ALWAYS);

        for (int i = 0; i < 2; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPercentWidth(i == 0 ? 30 : 70);
            node.getColumnConstraints().add(colConstraints);
        }

        for (int i = 0; i < 2; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(i == 0 ? 8 : 76);
            node.getRowConstraints().add(rowConstraints);
        }

        pathTextField.setOnMouseClicked(event -> {
            int clickCount = event.getClickCount();
            if (clickCount >= 2) {
                String path = DevToolsApplication.showDirectoryChooser();
                if (path != null) {
                    pathTextField.setText(path);
                }
            }
        });
    }

    @Override
    public Node getNode() {
        return node;
    }

    @Override
    public void adaptiveWidthHeight(Pane parent) {
        node.setLayoutX(parent.getPrefWidth() * 0.05);
        node.setLayoutY(parent.getPrefHeight() * 0.05);
        node.setPrefWidth(parent.getPrefWidth() * 0.9);
        node.setPrefHeight(parent.getPrefHeight() * 0.9);
        mdTextField.setPrefWidth(Double.MAX_VALUE);
        mdTextField.setPrefHeight(Double.MAX_VALUE);
    }

    public void switchInclude() {
        mode = "include";
        suffixEditArea.getChildren().removeAll(fileSuffix);
        fileSuffix.clear();
        List<String> includeSuffix = List.of("java", "xml", "properties", "yaml", "yml");
        for (String suffix : includeSuffix) {
            TextField textField = new TextField(suffix);
            suffixEditArea.getChildren().add(textField);
            fileSuffix.add(textField);
        }
    }

    public void switchExclude() {
        mode = "exclude";
        suffixEditArea.getChildren().removeAll(fileSuffix);
        fileSuffix.clear();
        List<String> excludeSuffix = List.of("png", "jpg", "jpeg", "gif", "bmp", "svg", "mp3", "wav", "mp4", "avi", "LICENSE", "class", "dir:target");
        for (String suffix : excludeSuffix) {
            TextField textField = new TextField(suffix);
            suffixEditArea.getChildren().add(textField);
            fileSuffix.add(textField);
        }
    }

    public String getMdText() {
        String absoluteDirectory = pathTextField.getText();
        Path basePath = Paths.get(absoluteDirectory);
        List<String> markdownLines = new ArrayList<>();
        List<String> fileSuffixList = fileSuffix.stream().map(TextField::getText).collect(Collectors.toList());
        List<String> dirList = new ArrayList<>();

        for (int i = 0; i < fileSuffixList.size(); i++) {
            String suffix = fileSuffixList.get(i);
            if (suffix.startsWith("dir:")) {
                dirList.add(suffix.substring(4));
                fileSuffixList.remove(i);
                i--;
            }
        }

        try {
            Files.walk(basePath)
                    .filter(Files::isRegularFile)
                    .filter(p -> {
                        if (mode.equals("include")) {
                            return fileSuffixList.stream().anyMatch(suffix -> p.toString().endsWith(suffix));
                        } else {
                            boolean dirB = dirList.stream().anyMatch(dir -> p.toString().contains(dir));
                            boolean suffixB = fileSuffixList.stream().noneMatch(suffix -> p.toString().endsWith(suffix));
                            return dirB && suffixB;
                        }
                    })
                    .sorted(Comparator.comparingInt(p -> p.getNameCount()))
                    .forEach(path -> {
                        String relativePath = basePath.relativize(path).toString().replace("\\", "/");
                        markdownLines.add("### " + path.getFileName());
                        markdownLines.add("路径：" + relativePath);
                        try {
                            markdownLines.add("```\n" + new String(Files.readAllBytes(path)) + "\n```\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return String.join("\n", markdownLines);
    }
}
