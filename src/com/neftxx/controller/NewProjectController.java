package com.neftxx.controller;

import com.neftxx.util.DialogUtils;
import com.neftxx.util.FileManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class NewProjectController implements Initializable {
    @FXML
    private Button btnDirectoryChooser;
    @FXML
    private TextField txtNameProject;
    @FXML
    private TextField txtNameMainFile;
    @FXML
    private TextField txtParentPathProject;
    @FXML
    private Button btnOk;
    @FXML
    private Button btnCancel;

    @FXML static NewProjectController projectController;

    private File projectPathFile;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        projectController = this;
        projectPathFile = null;
        txtParentPathProject.setText(System.getProperty("user.dir"));
        btnDirectoryChooser.setOnAction(actionEvent -> onDirectoryChooser());
        btnOk.setOnAction(actionEvent -> createNewProject());
        btnCancel.setOnAction(actionEvent -> cancel());
    }

    private void onDirectoryChooser() {
        projectPathFile = FileManager.openSourceDir("Elegir ruta destino del proyecto");
        if (projectPathFile != null && projectPathFile.exists()) {
            txtParentPathProject.setText(projectPathFile.getAbsolutePath());
        }
    }

    private void createNewProject() {
        String projectName = txtNameProject.getText().trim();
        String nameMainFile = txtNameMainFile.getText().trim();
        String projectPath = txtParentPathProject.getText().trim();

        boolean isProjectNameEmpty = projectName.isEmpty();
        boolean isNameMainFileEmpty = nameMainFile.isEmpty();
        boolean isProjectPathInvalid = !(projectPath.isEmpty() || projectPathFile.canWrite());
        if (isProjectPathInvalid) {
            String errorMessage = "Path del archivo no valido.";
            DialogUtils.createErrorDialog(DialogUtils.ERROR_DIALOG,null,errorMessage);
            return;
        }
        projectName = isProjectNameEmpty ? "newProject" : projectName;
        nameMainFile = isNameMainFileEmpty ? "main.r" : nameMainFile;
        boolean isExtension = FilenameUtils.isExtension(nameMainFile, new String[]{ "r", "R" });
        nameMainFile = isExtension ? nameMainFile : nameMainFile.concat(".r");
        String projectFullPath = FilenameUtils.concat(projectPath, projectName);
        String projectSrcPath = FilenameUtils.concat(projectFullPath, "src");
        String mainFilePath = FilenameUtils.concat(projectSrcPath, nameMainFile);
        File projectDir = FileManager.createNewFolder(projectFullPath);
        FileManager.createNewFolder(projectSrcPath);
        File mainFile = FileManager.createNewFile(mainFilePath);
        String textMainFunction = "zro main() {\n\t_imp(\"hello world!!\");\n}";
        FileManager.updateContent(mainFile, textMainFunction);
        MainController mainController = MainController.getInstance();
        mainController.pathMainFile = mainFilePath;
        mainController.openProjectMenuAction(projectDir);
        cancel();
    }

    private void cancel() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}
