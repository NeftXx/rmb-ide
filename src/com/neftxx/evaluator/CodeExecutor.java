package com.neftxx.evaluator;

import com.neftxx.ast.Program;
import com.neftxx.constant.Extension;
import com.neftxx.scope.FileScope;
import com.neftxx.util.DialogUtils;
import java_cup.runtime.ComplexSymbolFactory;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Objects;

public class CodeExecutor {
    private static final Hashtable<String, FileScope> FILE_SCOPE_HASHTABLE = new Hashtable<>();
    public static FileScope mainFileScope;

    public static String analyzeProject(File file) throws Exception {
        if (file != null) {
            FILE_SCOPE_HASHTABLE.clear();
            FileScope.clearSymbolsNode();
            if (file.isFile()) {
                if (Extension.isFileR(file.getAbsolutePath())) {
                    mainFileScope = evaluate(file);
                    if (mainFileScope != null) {
                        mainFileScope.runMain();
                    }
                    return file.getAbsolutePath();
                }
                DialogUtils.createWarningDialog(DialogUtils.WARNING_DIALOG, "Archivo principal",
                        "El archivo principal no tiene extension .r");
                return null;
            } else {
                ArrayList<File> files = getFiles(file);
                if (files.isEmpty()) {
                    DialogUtils.createWarningDialog(DialogUtils.WARNING_DIALOG, "Archivo principal",
                            "En este directorio no existe un archivo con extension R.");
                } else {
                    File mainFile = getMainFile(files);
                    mainFileScope = evaluate(mainFile);
                    if (mainFileScope != null) {
                        mainFileScope.runMain();
                    }
                    return mainFile.getAbsolutePath();
                }
            }
        }
        return null;
    }

    private static void addFileScope(String path, FileScope fileScope) {
        FILE_SCOPE_HASHTABLE.put(path, fileScope);
    }

    private static FileScope getFileScope(String path) {
        return FILE_SCOPE_HASHTABLE.get(path);
    }

    private static ArrayList<File> getFiles(File directory) {
        ArrayList<File> files = new ArrayList<>();
        for (File file: Objects.requireNonNull(directory.listFiles())) {
            if (file.isDirectory()) {
                files.addAll(getFiles(file));
            } else {
                if (Extension.isFileR(file.getName())) {
                    files.add(file);
                }
            }
        }
        return files;
    }

    private static File selectFile;

    private static File getMainFile(ArrayList<File> files) {
        Stage stage = new Stage();
        Label label = new Label("Seleccionar archivo principal: ");
        ObservableList<File> list = FXCollections.observableArrayList(files);
        ListView<File> fileListView = new ListView<>(list);
        fileListView.setOrientation(Orientation.VERTICAL);
        fileListView.getSelectionModel().selectedItemProperty().addListener(CodeExecutor::getSelectFile);
        Button button = new Button("Ok");
        button.setMinWidth(100);
        button.setOnAction(actionEvent -> stage.close());
        VBox vBox = new VBox();
        vBox.setMinWidth(450);
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(label, fileListView, button);
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(10);
        pane.setVgap(5);
        pane.addColumn(0, vBox);
        Scene scene = new Scene(pane, 500, 400);
        stage.setScene(scene);
        stage.setTitle("Seleccionar archivo");
        stage.showAndWait();
        return selectFile;
    }

    private static void getSelectFile(ObservableValue<? extends File> observableValue, File oldValue, File newValue) {
        selectFile = newValue;
    }

    public static FileScope evaluate(File file) throws Exception {
        var fileScope = getFileScope(file.getAbsolutePath());
        if (fileScope == null) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            ComplexSymbolFactory sf = new ComplexSymbolFactory();
            Parser parse = new Parser(new Lexer(reader, sf, file.getAbsolutePath()), sf);
            parse.parse();
            Program program = parse.program;
            if (program != null) {
                fileScope = program.createFileScope();
                addFileScope(file.getAbsolutePath(), fileScope);
            }
        }
        return fileScope;
    }
}
