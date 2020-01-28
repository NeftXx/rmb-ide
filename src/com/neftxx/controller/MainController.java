package com.neftxx.controller;

import com.neftxx.App;
import com.neftxx.constant.Extension;
import com.neftxx.constant.Icons;
import com.neftxx.error.ErrorHandler;
import com.neftxx.error.NodeError;
import com.neftxx.evaluator.CodeExecutor;
import com.neftxx.evaluator.Source;
import com.neftxx.olc.Lexer;
import com.neftxx.olc.Parser;
import com.neftxx.scope.FileScope;
import com.neftxx.util.*;
import com.neftxx.util.Console;
import java_cup.runtime.ComplexSymbolFactory;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class MainController implements Initializable {
    @FXML
    private MenuItem consoleClearMenuItem;
    @FXML
    private MenuItem tableErrorsClearMenuItem;
    @FXML
    private MenuItem tableSymbolsClearMenuItem;
    @FXML
    private MenuItem allClearMenuItem;
    @FXML
    private Button saveFolder;
    @FXML
    private MenuItem aboutMenuItem;
    @FXML
    private TableView<SymbolNode> tableSymbols;
    @FXML
    private TableColumn<String, SymbolNode> columnNameSym;
    @FXML
    private TableColumn<String, SymbolNode> columnTypeSym;
    @FXML
    private TableColumn<String, SymbolNode> columnScopeSym;
    @FXML
    private TableColumn<String, SymbolNode> columnRolSym;
    @FXML
    private TableColumn<String, SymbolNode> columnParameterSym;
    @FXML
    private TableColumn<String, SymbolNode> columnConstantSym;
    @FXML
    private TabPane tabReportPane;
    @FXML
    private MenuItem newFileMenuItem;
    @FXML
    private MenuItem newProjectMenuItem;
    @FXML
    private MenuItem openFileMenuItem;
    @FXML
    private MenuItem openProjectMenuItem;
    @FXML
    private MenuItem closeProjectMenuItem;
    @FXML
    private MenuItem exitMenuItem;
    @FXML
    private Button openFileButton;
    @FXML
    private Button newFileButton;
    @FXML
    private Button saveFileButton;
    @FXML
    private Button newProjectButton;
    @FXML
    private Button openProjectButton;
    @FXML
    private Button startButton;
    @FXML
    private TreeView<Source> filesTreeView;
    @FXML
    private TabPane codeAreaLayout;
    @FXML
    public TextArea consoleTextArea;
    @FXML
    private TableView<NodeError> tableErrors;
    @FXML
    private TableColumn<String, NodeError> columnTypeError;
    @FXML
    private TableColumn<String, NodeError> columnDescription;
    @FXML
    private TableColumn<String, NodeError> columnLine;
    @FXML
    private TableColumn<String, NodeError> columnColumn;
    @FXML
    private TableColumn<String, NodeError> columnFile;

    private File currentDirectoryProject;
    public String pathMainFile;
    private ExecutorService executorService;
    private static Logger debugger;
    private static final int NOT_FOUND_INDEX = -1;
    private static final String DEBUG_TAG = MainController.class.getSimpleName();
    private static final int THREAD_AVAILABLE_NUMBER = Runtime.getRuntime().availableProcessors();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static MainController instance;

    private static final int CONSOLE_TAB = 0;
    private static final int TABLE_ERRORS_TAB = 1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
        currentDirectoryProject = null;
        pathMainFile = "";
        executorService = Executors.newFixedThreadPool(THREAD_AVAILABLE_NUMBER);
        debugger = Logger.getLogger(DEBUG_TAG);
        onMenuItemsActions();
        initTableErrors();
        initTableSymbols();
        codeAreaLayout.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        filesTreeView.setOnMouseClicked(this::onMouseClickedFilesTreeView);
    }

    private void initTableErrors() {
        tableErrors.setPlaceholder(new Label("No hay errores que mostrar."));
        columnTypeError.setCellValueFactory(new PropertyValueFactory<>("typeError"));
        columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        columnLine.setCellValueFactory(new PropertyValueFactory<>("line"));
        columnColumn.setCellValueFactory(new PropertyValueFactory<>("column"));
        columnFile.setCellValueFactory(new PropertyValueFactory<>("filename"));
    }

    private void initTableSymbols() {
        tableSymbols.setPlaceholder(new Label("No hay simbolos que mostrar"));
        columnNameSym.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnTypeSym.setCellValueFactory(new PropertyValueFactory<>("type"));
        columnScopeSym.setCellValueFactory(new PropertyValueFactory<>("scope"));
        columnRolSym.setCellValueFactory(new PropertyValueFactory<>("rol"));
        columnParameterSym.setCellValueFactory(new PropertyValueFactory<>("parameter"));
        columnConstantSym.setCellValueFactory(new PropertyValueFactory<>("isConstant"));
    }

    /**
     * Acciones de los menus y botones
     */
    private void onMenuItemsActions() {
        aboutMenuItem.setOnAction(actionEvent ->
                DialogUtils.createInformationDialog("Sobre", "Ronald Berdúo", "201504420"));
        openFileMenuItem.setOnAction(event -> onOpenFileMenuAction());
        openFileButton.setOnAction(actionEvent -> onOpenFileMenuAction());
        openProjectMenuItem.setOnAction(event -> onOpenProjectMenuAction());
        openProjectButton.setOnAction(event -> onOpenProjectMenuAction());
        newFileMenuItem.setOnAction(event -> showNewFileDialog());
        newFileButton.setOnAction(event -> showNewFileDialog());
        newProjectMenuItem.setOnAction(actionEvent -> onNewProjectMenuAction());
        newProjectButton.setOnAction(actionEvent -> onNewProjectMenuAction());
        saveFileButton.setOnAction(actionEvent -> onSaveFileMenuAction());
        saveFolder.setOnAction(actionEvent -> saveProject());
        startButton.setOnAction(actionEvent -> runProject());
        closeProjectMenuItem.setOnAction(event -> onCloseMenuAction());
        exitMenuItem.setOnAction(event -> onExitMenuAction());
        consoleClearMenuItem.setOnAction(actionEvent -> consoleTextArea.clear());
        tableErrorsClearMenuItem.setOnAction(actionEvent -> tableErrors.getItems().clear());
        tableSymbolsClearMenuItem.setOnAction(actionEvent -> tableSymbols.getItems().clear());
        allClearMenuItem.setOnAction(actionEvent -> {
            consoleTextArea.clear();
            tableErrors.getItems().clear();
            tableSymbols.getItems().clear();
        });
    }

    /**
     * Guarda el archivo del tab actual
     */
    private void onSaveFileMenuAction() {
        if (!codeAreaLayout.getTabs().isEmpty()) {
            Tab tab = codeAreaLayout.getSelectionModel().getSelectedItem();
            String text;
            if (tab.getContent() instanceof TextArea) {
                TextArea textArea = (TextArea) tab.getContent();
                text = textArea.getText();
            } else {
                var pane = (VirtualizedScrollPane<?>) tab.getContent();
                CodeArea codeArea = (CodeArea) pane.getContent();
                text = codeArea.getText();
            }
            File file = new File(tab.getId());
            FileManager.updateContent(file, text);
            DialogUtils.createInformationDialog("Guardar archivo", "Archivo guardado.",
                    "Se guardo el archivo " + tab.getText() + " con éxito");
        }
    }

    private void onNewProjectMenuAction() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("view/new_project.fxml"));
            Stage stage = new Stage();
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Nuevo proyecto");
            stage.setScene(scene);
            stage.setOnCloseRequest(event -> System.exit(0));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void runProject() {
        if (currentDirectoryProject != null) {
            Console.builder.setLength(0);
            consoleTextArea.clear();
            tableErrors.getItems().clear();
            tableSymbols.getItems().clear();
            if (pathMainFile.isEmpty()) {
                runFolder();
            } else {
                File file = new File(pathMainFile);
                if (file.exists()) {
                    try {
                        CodeExecutor.analyzeProject(file);
                    } catch (Exception e) {
                        String warnMessage = "Se provoco un error al analizar el proyecto actual";
                        debugger.warning(warnMessage);
                        DialogUtils.createErrorDialog(DialogUtils.ERROR_DIALOG, warnMessage, e.getMessage());
                        e.printStackTrace();
                        showErrorsInTableView();
                    }
                } else {
                    runFolder();
                }
            }
        }
    }

    private void runFolder() {
        TreeItem<Source> root = filesTreeView.getRoot();
        if (Objects.nonNull(root)) {
            try {
                Source source = root.getValue();
                pathMainFile = CodeExecutor.analyzeProject(source.getFile());
                if (ErrorHandler.getErrors().isEmpty()) {
                    tableErrors.getItems().clear();
                    tabReportPane.getSelectionModel().select(CONSOLE_TAB);
                } else {
                    showErrorsInTableView();
                }
                showSymbolsTableView();
            } catch (Exception e) {
                String warnMessage = "Se provoco un error al analizar el proyecto actual";
                debugger.warning(warnMessage);
                DialogUtils.createErrorDialog(DialogUtils.ERROR_DIALOG, warnMessage, e.getMessage());
                e.printStackTrace();
                showErrorsInTableView();
            }
            ErrorHandler.getErrors().clear();
        }
    }

    private void saveProject() {
        if (currentDirectoryProject != null) {
            String name = currentDirectoryProject.getName() + ".olc";
            String path = FilenameUtils.concat(currentDirectoryProject.getAbsolutePath(), name);
            File configurationFile = new File(path);
            if (configurationFile.exists()) {
                createTextConfiguration(currentDirectoryProject, configurationFile);
            } else {
                boolean notCreate = !createConfigurationFile(currentDirectoryProject, path);
                if (notCreate) {
                    DialogUtils.createErrorDialog("Error", "Error",
                            "Error al guardar el archivo de configuracion");
                }
            }
        }
    }

    /**
     * Muestra un dialogo para crear un nuevo archivo
     */
    private void showNewFileDialog() {
        if (Objects.nonNull(filesTreeView.getTreeItem(0))) {
            int directoryIndex = filesTreeView.getSelectionModel().getSelectedIndex();
            if (directoryIndex != NOT_FOUND_INDEX) {
                TreeItem<Source> directory = filesTreeView.getSelectionModel().getSelectedItem();
                Source dirSource = directory.getValue();
                File dirFile = dirSource.getFile();
                String dirPath = "";
                if (dirFile.isDirectory() && dirFile.canWrite()) {
                    dirPath = dirFile.getPath();
                } else if (dirFile.getParentFile().canWrite()) {
                    dirPath = dirFile.getParentFile().getPath();
                }
                File newFile = FileManager.createNewFileAndSave(dirPath);
                boolean ok = createNewFileAndSave(newFile);
                if (ok) {
                    updateFilesTreeView();
                } else {
                    String warnMessage = "No se pudo crear el archivo";
                    debugger.warning(warnMessage);
                    DialogUtils.createErrorDialog(DialogUtils.ERROR_DIALOG, warnMessage, "");
                }
            } else {
                File newFile = FileManager.createNewFileAndSave();
                boolean notCreate = !createNewFileAndSave(newFile);
                if (notCreate) {
                    String warnMessage = "No se pudo crear el archivo";
                    debugger.warning(warnMessage);
                    DialogUtils.createErrorDialog(DialogUtils.ERROR_DIALOG, warnMessage, "");
                }
            }
        } else {
            File newFile = FileManager.createNewFileAndSave();
            boolean notCreate = !createNewFileAndSave(newFile);
            if (notCreate) {
                String warnMessage = "No se pudo crear el archivo";
                debugger.warning(warnMessage);
                DialogUtils.createErrorDialog(DialogUtils.ERROR_DIALOG, warnMessage, "");
            }
        }
    }

    private boolean createNewFileAndSave(File newFile) {
        if (newFile != null) {
            try {
                boolean ok = newFile.createNewFile();
                if (ok) {
                    if (Extension.isExtensionRmb(newFile.getName())) {
                        openSourceInTab(newFile);
                    } else {
                        openTextInTab(newFile);
                    }
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Muestra un FileChooser para seleccionar un archivo, para poder abrirlo
     */
    private void onOpenFileMenuAction() {
        File outputFile = FileManager.openSourceFile("Abrir archivo Rmb");
        if (outputFile != null)
            if (Extension.isExtensionRmb(outputFile.getName()))
                executorService.execute(() -> openSourceInTab(outputFile));
    }

    /**
     * Abre un proyecto y verifica si existe un archivo de configuracion
     */
    private void onOpenProjectMenuAction() {
        File sourceFolder = FileManager.openSourceDir("Abrir Proyecto");
        if (Objects.nonNull(sourceFolder)) {
            FilenameFilter filter = (dir, name) -> Extension.isOlcFile(name);
            String[] children = sourceFolder.list(filter);
            if (children == null || children.length == 0) {
                DialogUtils.createConfirmDialog("Configuración", "Crear archivo de configuración",
                        "No se encuentra archivo de configuración, ¿Desea crearlo?", () -> {
                            String configurationFile = sourceFolder.getName() + ".olc";
                            String path = FilenameUtils.concat(sourceFolder.getAbsolutePath(), configurationFile);
                            boolean ok = createConfigurationFile(sourceFolder, path);
                            if (ok) {
                                currentDirectoryProject = sourceFolder;
                            } else {
                                DialogUtils.createErrorDialog(DialogUtils.ERROR_DIALOG, "Error",
                                        "No se pudo crear el archivo de configuración.");
                            }
                        });
            } else {
                String path = "";
                for (var filename: children) {
                    path = FilenameUtils.concat(sourceFolder.getAbsolutePath(), filename);
                    if (sourceFolder.getName().equals(FilenameUtils.removeExtension(filename))) {
                        File configurationFile = new File(path);
                        boolean ok = readConfigurationFile(configurationFile);
                        if (ok) {
                            currentDirectoryProject = sourceFolder;
                            return;
                        }
                        break;
                    } else {
                        FileManager.deleteFile(path);
                    }
                }
                path = path.equals("") ? sourceFolder.getName() + ".olc" : path;
                boolean ok = createConfigurationFile(sourceFolder, path);
                if (ok) {
                    currentDirectoryProject = sourceFolder;
                } else {
                    DialogUtils.createErrorDialog("Error", "Error",
                            "Error al leer el archivo de configuracion");
                }
            }
        }
    }

    public void openProjectMenuAction(File sourceFolder) {
        if (Objects.nonNull(sourceFolder)) {
            String name = sourceFolder.getName() + ".olc";
            String path = FilenameUtils.concat(sourceFolder.getAbsolutePath(), name);
            currentDirectoryProject = sourceFolder;
            boolean ok = createConfigurationFile(sourceFolder, path);
            if (!ok) {
                currentDirectoryProject = null;
            }
        }
    }

    /**
     * Crea un archivo de configuracion
     *
     * @param sourceFolder directorio del proyecto
     * @param path path del archivo de configuracion
     */
    private boolean createConfigurationFile(File sourceFolder, String path) {
        File configurationFile = FileManager.createNewFile(path);
        if (Objects.nonNull(configurationFile)) {
            createTextConfiguration(sourceFolder, configurationFile);
            return true;
        }
        return false;
    }

    private void createTextConfiguration(File sourceFolder, File configurationFile) {
        StringBuilder stringBuilder = new StringBuilder();
        String pathTemp = "";
        if (this.currentDirectoryProject != null && !pathMainFile.isEmpty()) {
            String absolute = currentDirectoryProject.getAbsolutePath();
            pathTemp = pathMainFile.replace(absolute, "");
        }
        stringBuilder.append("{\n\tproyecto: {\n\t\truta: \"")
                .append(sourceFolder.getAbsolutePath()).append("\",\n")
                .append("\t\tnombre: \"").append(sourceFolder.getName())
                .append("\",\n").append("\t\tcorrer: \"")
                .append(pathTemp).append("\",\n\t\tconfiguracion: {\n");
        TreeItem<Source> root = getFilesForDirectory(sourceFolder, stringBuilder);
        stringBuilder.append("\t\t}\n\t}\n}");
        FileManager.updateContent(configurationFile, stringBuilder.toString());
        updateFilesTreeView(root);
    }

    private boolean readConfigurationFile(File configurationFile) {
        if (Objects.nonNull(configurationFile)) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(configurationFile));
                ComplexSymbolFactory sf = new ComplexSymbolFactory();
                Parser parse = new Parser(new Lexer(reader, sf, configurationFile.getAbsolutePath()), sf);
                parse.parse();
                if (parse.olc == null) return false;
                var olc = parse.olc;
                var treeItem = olc.getFilesForProject(configurationFile.getAbsolutePath());
                pathMainFile = olc.getRunPath();
                updateFilesTreeView(treeItem);
                reader.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                String warnMessage = "Error al intentar leer el archivo de configuracion.";
                debugger.warning(warnMessage);
                DialogUtils.createErrorDialog(DialogUtils.WARNING_DIALOG, warnMessage, e.getMessage());
                showErrorsInTableView();
                ErrorHandler.getErrors().clear();
                return false;
            }
        }
        return false;
    }

    /**
     * Crea los items del treeView y a la vez el texto del archivo de configuracion
     *
     * @param directory directorio
     * @param stringBuilder concatena la cadena resultante
     * @return TreeItem<Source>
     */
    synchronized private TreeItem<Source> getFilesForDirectory(File directory, StringBuilder stringBuilder) {
        TreeItem<Source> root = new TreeItem<>(new Source(directory));
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isDirectory()) {
                stringBuilder.append("carpeta: {\n").append("nombre: \"").append(file.getName()).append("\",\n");
                TreeItem<Source> treeItem = getFilesForDirectory(file, stringBuilder);
                stringBuilder.append("},\n");
                treeItem.setGraphic(ImageUtils.buildImageView(Icons.folderIconImage));
                root.getChildren().add(treeItem);
            } else {
                    stringBuilder.append("archivo: {\n").append("nombre: \"").append(file.getName()).append("\",\n")
                            .append("fecha_mod: \"").append(DATE_FORMAT.format(file.lastModified())).append("\"\n},\n");
                if (Extension.isExtensionRmb(file.getName())) {
                    root.getChildren().add(new TreeItem<>(new Source(file), ImageUtils.buildImageView(Icons.codeIconImage)));
                } else {
                    root.getChildren().add(new TreeItem<>(new Source(file), ImageUtils.buildImageView(Icons.textIconImage)));
                }
            }
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 2);
        return root;
    }

    synchronized private TreeItem<Source> getFilesForDirectory(File directory) {
        TreeItem<Source> root = new TreeItem<>(new Source(directory));
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isDirectory()) {
                TreeItem<Source> treeItem = getFilesForDirectory(file);
                treeItem.setGraphic(ImageUtils.buildImageView(Icons.folderIconImage));
                root.getChildren().add(treeItem);
            } else {
                if (Extension.isExtensionRmb(file.getName())) {
                    root.getChildren().add(new TreeItem<>(new Source(file), ImageUtils.buildImageView(Icons.codeIconImage)));
                } else {
                    root.getChildren().add(new TreeItem<>(new Source(file), ImageUtils.buildImageView(Icons.textIconImage)));
                }
            }
        }
        return root;
    }

    private void updateFilesTreeView() {
        if (currentDirectoryProject != null) {
            TreeItem<Source> root = getFilesForDirectory(currentDirectoryProject);
            updateFilesTreeView(root);
        }
    }

    /**
     * Actualiza el treeView
     *
     * @param sourceTreeItem raiz
     */
    private void updateFilesTreeView(TreeItem<Source> sourceTreeItem) {
        if (Objects.nonNull(sourceTreeItem)) {
            sourceTreeItem.setGraphic(ImageUtils.buildImageView(Icons.folderIconImage));
            executorService.submit(() -> Platform.runLater(() -> filesTreeView.setRoot(sourceTreeItem)));
        }
    }

    /**
     * Lee un archivo RMB y lo abre en un CodeArea
     *
     * @param sourceFile archivo
     */
    private void openSourceInTab(File sourceFile) {
        if (Objects.nonNull(sourceFile)) {
            boolean exists = selectTabIfItExists(sourceFile.getAbsolutePath());
            if (!exists) {
                Tab rmbTab = new Tab(sourceFile.getName());
                rmbTab.setId(sourceFile.getAbsolutePath());
                rmbTab.setUserData(sourceFile.getPath());
                rmbTab.setOnClosed(event -> onTabCloseAction(rmbTab));
                rmbTab.setGraphic(ImageUtils.buildImageView(Icons.codeIconImage));

                try {
                    var code = readFile(sourceFile);
                    CodeArea codeTextArea = new CodeArea();
                    EditorController editorController = new EditorController(codeTextArea);
                    editorController.editorSettings();
                    codeTextArea.replaceText(0, 0, code);
                    rmbTab.setContent(new VirtualizedScrollPane<>(codeTextArea));
                    Platform.runLater(() -> {
                        codeAreaLayout.getTabs().add(rmbTab);
                        codeAreaLayout.getSelectionModel().select(rmbTab);
                    });
                } catch (IOException e) {
                    String warnMessage = "No se pudo abrir el archivo";
                    debugger.warning(warnMessage);
                    DialogUtils.createWarningDialog(DialogUtils.WARNING_DIALOG, null, warnMessage);
                }
            }
        }
    }

    /**
     * Abre un archivo cualquiera en un textArea
     *
     * @param textFile archivo
     */
    private void openTextInTab(File textFile) {
        if (Objects.nonNull(textFile)) {
            boolean exists = selectTabIfItExists(textFile.getAbsolutePath());
            if (!exists) {
                Tab textTab = new Tab(textFile.getName());
                textTab.setId(textFile.getAbsolutePath());
                textTab.setUserData(textFile.getPath());
                textTab.setOnClosed(event -> onTabCloseAction(textTab));
                textTab.setGraphic(ImageUtils.buildImageView(Icons.textIconImage));

                try {
                    TextArea textArea = new TextArea();
                    var text = readFile(textFile);
                    textArea.replaceText(0, 0, text);
                    textTab.setContent(textArea);
                    Platform.runLater(() -> {
                        codeAreaLayout.getTabs().add(textTab);
                        codeAreaLayout.getSelectionModel().select(textTab);
                    });
                } catch (IOException e) {
                    String warnMessage = "No se pudo abrir el archivo";
                    debugger.warning(warnMessage);
                    DialogUtils.createWarningDialog(DialogUtils.WARNING_DIALOG, null, warnMessage);
                }
            }
        }
    }

    /**
     * Verifica si un archivo ya esta abierto en el editor
     *
     * @param absolutePath ruta
     * @return boolean
     */
    private boolean selectTabIfItExists(String absolutePath) {
        ObservableList<Tab> tabs = codeAreaLayout.getTabs();
        for (Tab tab: tabs) {
            if (tab.getId().equals(absolutePath)) {
                SingleSelectionModel<Tab> selectionModel = codeAreaLayout.getSelectionModel();
                selectionModel.select(tab);
                return true;
            }
        }
        return false;
    }

    /**
     * Lee un archivo
     *
     * @param file archivo
     * @return String
     * @throws IOException error al leer archivo
     */
    private String readFile(File file) throws IOException {
        return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    }

    /**
     * Limpia el codeArea cuando se cierra el tab
     *
     * @param tab Tab
     */
    private void onTabCloseAction(Tab tab) {
        if (Objects.nonNull(tab)) {
            String tabName = tab.getText();
            boolean isRmbFile = Extension.isExtensionRmb(tabName);
            if (isRmbFile) {
                CodeArea codeArea = (CodeArea) ((Parent) tab.getContent()).getChildrenUnmodifiable().get(0);
                codeArea.displaceCaret(0);
            }
        }
    }

    /**
     * Muestra los errores en la tabla
     */
    private void showErrorsInTableView() {
        tableErrors.getItems().clear();
        var list = ErrorHandler.getErrors();
        for (var error : list) {
            tableErrors.getItems().add(error);
        }
        tabReportPane.getSelectionModel().select(TABLE_ERRORS_TAB);
    }

    /**
     * Muestra los simbolos encontrados
     */
    private void showSymbolsTableView() {
        tableSymbols.getItems().clear();
        var list = FileScope.SYMBOL_NODES;
        for (var symbol: list) {
            tableSymbols.getItems().add(symbol);
        }
    }

    /**
     * Al cerrar proyecto
     */
    private void onCloseMenuAction() {
        if (this.currentDirectoryProject != null) {
            filesTreeView.setRoot(null);
            codeAreaLayout.getTabs().clear();
            consoleTextArea.setText("");
            tableErrors.getItems().clear();
            tableSymbols.getItems().clear();
            pathMainFile = "";
            currentDirectoryProject = null;
        }
    }

    public static MainController getInstance() {
        return instance;
    }

    /**
     * para cerrar la aplicacion
     */
    private void onExitMenuAction() {
        saveProject();
        Platform.exit();
        System.exit(0);
    }

    /**
     * Al presionar doble click abre el archivo en el tab
     *
     * @param mouseEvent evento
     */
    private void onMouseClickedFilesTreeView(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            TreeItem<Source> newValue = filesTreeView.getSelectionModel().getSelectedItem();
            if (Objects.nonNull(newValue)) {
                String fileName = newValue.getValue().toString();
                if (Extension.isExtensionRmb(fileName)) {
                    Source rmbSource = newValue.getValue();
                    openSourceInTab(rmbSource.getFile());
                } else {
                    Source textSource = newValue.getValue();
                    if (textSource.getFile().isFile()) {
                        openTextInTab(textSource.getFile());
                    }
                }
            }
        }
    }
}
