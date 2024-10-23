package org.xiaoxve.fun.project;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.xiaoxve.DevToolsApplication;
import org.xiaoxve.itfc.Fun;

import java.io.*;
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
    VBox dirEditArea;
    VBox mainEditArea;
    TextArea mdTextField;
    HBox hBox;
    String suffixMode;
    String dirMode;
    List<TextField> fileSuffix;
    List<TextField> dirPath;

    public ProjectToMdFun() {
        node = new GridPane();
        pathLabel = new Label("项目路径");
        pathTextField = new TextField();
        pathTextField.setPromptText("双击选择路径");
        suffixEditArea = new VBox();
        mdTextField = new TextArea();
        fileSuffix = new ArrayList<>();
        dirPath = new ArrayList<>();
        suffixMode = "include";
        pathTextField.setEditable(false);
        mdTextField.setEditable(false);
        node.setBorder(new Border(new BorderStroke(Color.LIGHTGREY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        node.setHgap(10);
        node.setVgap(10);

        // 设置背景颜色
        node.setStyle("-fx-background-color: #f9f9f9;");
        pathTextField.setStyle("-fx-border-color: #d3d3d3; -fx-border-radius: 5px;");
        mdTextField.setStyle("-fx-text-fill: #333; -fx-font-size: 14px; -fx-border-color: #d3d3d3; -fx-border-radius: 5px;");
        pathTextField.setOnMouseClicked(event -> {
            String path = DevToolsApplication.showDirectoryChooser();
            if (path != null) {
                pathTextField.setText(path);
            }
        });
        // 模式切换按钮
        Label fileModeLabel = new Label("后缀模式：");
        Button switchButton = new Button("包含");
        switchButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-radius: 5px;");

        // 开始按钮
        Button start = new Button("开始");
        start.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-border-radius: 5px;");
        start.setOnAction(event -> {
            String mdText = getMdText();
            mdTextField.setText(mdText);
        });

        // 添加按钮
        Button addButton = new Button("+");
        addButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-border-radius: 5px;");
        addButton.setOnAction(event -> {
            TextField textField = new TextField();
            suffixEditArea.getChildren().add(textField);
            fileSuffix.add(textField);
        });
        mainEditArea = new VBox();
        // 包含/排除模式切换
        hBox = new HBox(fileModeLabel, switchButton, addButton);
        hBox.setSpacing(20);
        suffixEditArea.getChildren().add(hBox);

        switchInclude();
        switchButton.setOnAction(event -> {
            if ("include".equals(suffixMode)) {
                switchButton.setText("排除");
                switchExclude();
            } else {
                switchButton.setText("包含");
                switchInclude();
            }
        });
        mainEditArea.getChildren().add(start);
        mainEditArea.getChildren().add(suffixEditArea);
        dirEditArea = new VBox();
        //dir同步
        Label dirModeLabel = new Label("目录模式：");
        Button dirSwitchButton = new Button("包含");
        dirSwitchButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-radius: 5px;");
        dirSwitchButton.setOnAction(event -> {
            if ("include".equals(dirMode)) {
                dirSwitchButton.setText("排除");
                switchDirExclude();
            } else {
                dirSwitchButton.setText("包含");
                switchDirInclude();
            }
        });
        dirEditArea.setSpacing(10);
        Button addDirButton = new Button("+");
        addDirButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-border-radius: 5px;");
        HBox dirHBox = new HBox(dirModeLabel, dirSwitchButton, addDirButton);
        dirEditArea.getChildren().add(dirHBox);
        addDirButton.setOnAction(event -> {
            TextField textField = new TextField();
            dirEditArea.getChildren().add(textField);
            dirPath.add(textField);
        });
        switchDirInclude();
        mainEditArea.getChildren().add(dirEditArea);
        ScrollPane scrollPane = new ScrollPane(mainEditArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        node.add(scrollPane, 0, 1, 1, 1);
        node.add(pathLabel, 0, 0);
        node.add(pathTextField, 1, 0);
        node.add(mdTextField, 1, 1);

        // 文件保存部分
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
        saveButton.setStyle("-fx-background-color: #607D8B; -fx-text-fill: white; -fx-border-radius: 5px;");
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
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "文件保存成功：" + outputPath);
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

        node.setPadding(new Insets(20));
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

        // 设置提示信息
        Tooltip tooltip = new Tooltip("双击选择文件路径");
        tooltip.setShowDelay(Duration.seconds(1));
        pathTextField.setTooltip(tooltip);
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

    private void switchInclude() {
        suffixMode = "include";
        suffixEditArea.getChildren().removeAll(fileSuffix);
        fileSuffix.clear();
        List<String> includeSuffix = List.of("java", "xml", "properties", "yaml", "yml");
        for (String suffix : includeSuffix) {
            TextField textField = new TextField(suffix);
            suffixEditArea.getChildren().add(textField);
            fileSuffix.add(textField);
        }
    }

    private void switchExclude() {
        suffixMode = "exclude";
        suffixEditArea.getChildren().removeAll(fileSuffix);
        fileSuffix.clear();
        List<String> excludeSuffix = List.of("png", "jpg", "jpeg", "gif", "bmp", "svg", "mp3", "wav", "mp4", "avi", "LICENSE", "class");
        for (String suffix : excludeSuffix) {
            TextField textField = new TextField(suffix);
            suffixEditArea.getChildren().add(textField);
            fileSuffix.add(textField);
        }
    }


    private void switchDirExclude(){
        dirMode = "exclude";
        dirEditArea.getChildren().removeAll(dirPath);
        dirPath.clear();
        List<String> excludeDir = List.of("/target", "/dist","/node_modules");
        for (String dir : excludeDir) {
            TextField textField = new TextField(dir);
            dirEditArea.getChildren().add(textField);
            dirPath.add(textField);
        }
    }
    private void switchDirInclude(){
        dirMode = "include";
        dirEditArea.getChildren().removeAll(dirPath);
        dirPath.clear();
        List<String> includeDir = List.of("/src");
        for (String dir : includeDir) {
            TextField textField = new TextField(dir);
            dirEditArea.getChildren().add(textField);
            dirPath.add(textField);
        }
    }

    private String getMdText() {
        String absoluteDirectory = pathTextField.getText();
        File file = new File(absoluteDirectory);
        file = new File(file.getAbsolutePath());
        StringBuilder md = new StringBuilder();
        List<String> fileSuffixList = fileSuffix.stream().map(TextField::getText).collect(Collectors.toList());
        List<String> dirList = dirPath.stream().map(TextField::getText).collect(Collectors.toList());
        for (File listFile : file.listFiles()) {
            try {
                recursiveCollection(file.getAbsolutePath(),listFile,md,dirList,fileSuffixList);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return md.toString();
    }
    private void recursiveCollection(String root,File file,StringBuilder md,List<String> dirList,List<String> fileSuffixList) throws IOException {
        if (file.isDirectory()) {
            if (dirList.size()>0){
                if (dirMode.equals("include")) {
                    String relativePath = file.getAbsolutePath().replace(root, "");
                    if (dirList.stream().anyMatch(relativePath::contains)||dirList.stream().anyMatch(dir -> relativePath.replaceAll("\\\\","/").contains(dir))||dirList.stream().anyMatch(dir -> relativePath.replaceAll("/","\\").contains(dir))) {
                        for (File listFile : file.listFiles()) {
                            recursiveCollection(root,listFile,md,dirList,fileSuffixList);
                        }
                    }
                }else{
                    String relativePath = file.getAbsolutePath().replace(root, "");
                    if (dirList.stream().noneMatch(dir -> relativePath.contains(dir))) {
                        for (File listFile : file.listFiles()) {
                            recursiveCollection(root,listFile,md,dirList,fileSuffixList);
                        }
                    }
                }
            }else{
                for (File listFile : file.listFiles()) {
                    recursiveCollection(root,listFile,md,dirList,fileSuffixList);
                }
            }
        }else {
            String relativePath = file.getAbsolutePath().replace(root, "");
            if (fileSuffixList.size()>0){
                if (suffixMode.equals("include")){
                    if (fileSuffixList.stream().anyMatch(suffix -> relativePath.endsWith(suffix))) {
                        addToMd(file, md, relativePath);
                    }
                }else{
                    if (fileSuffixList.stream().noneMatch(suffix -> relativePath.endsWith(suffix))) {
                        addToMd(file, md, relativePath);
                    }
                }
            }else{
                addToMd(file, md, relativePath);
            }
        }
    }

    private void addToMd(File file, StringBuilder md, String relativePath) throws IOException {
        md.append("### ").append(relativePath).append("\n").append(file.getName()).append("\n");
        md.append("```").append("\n");
        md.append(new String(new BufferedInputStream(new FileInputStream(file)).readAllBytes())).append("\n");
        md.append("\n").append("```").append("\n");
    }

//    String absoluteDirectory = pathTextField.getText();
//    Path basePath = Paths.get(absoluteDirectory);
//    List<String> markdownLines = new ArrayList<>();
//    List<String> fileSuffixList = fileSuffix.stream().map(TextField::getText).collect(Collectors.toList());
//    List<String> dirList = new ArrayList<>();
//
//        for (int i = 0; i < fileSuffixList.size(); i++) {
//        String suffix = fileSuffixList.get(i);
//        if (suffix.startsWith("dir:")) {
//            dirList.add(suffix.substring(4));
//            fileSuffixList.remove(i);
//            i--;
//        }
//    }
//        try {
//        Files.walk(basePath)
//                .filter(Files::isRegularFile)
//                .filter(p -> {
//                    if ("include".equals(suffixMode)) {
//                        return fileSuffixList.stream().anyMatch(suffix -> p.toString().endsWith(suffix));
//                    } else {
//                        boolean dirB = dirList.stream().anyMatch(dir -> p.toString().contains(dir));
//                        boolean suffixB = fileSuffixList.stream().noneMatch(suffix -> p.toString().endsWith(suffix));
//                        return dirB && suffixB;
//                    }
//                })
//                .sorted(Comparator.comparingInt(p -> p.getNameCount()))
//                .forEach(path -> {
//                    String relativePath = basePath.relativize(path).toString().replace("\\", "/");
//                    markdownLines.add("### " + path.getFileName());
//                    markdownLines.add("路径：" + relativePath);
//                    try {
//                        markdownLines.add("```\n" + new String(Files.readAllBytes(path)) + "\n```\n");
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                });
//    } catch (IOException e) {
//        e.printStackTrace();
//    }
//        return String.join("\n", markdownLines);
}