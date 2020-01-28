package com.neftxx.util;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {

    private static final FileChooser.ExtensionFilter RMB_FILES =
            new FileChooser.ExtensionFilter("Rmb Files", "*.r", "*.m", "*.b");
    private static final FileChooser.ExtensionFilter ALL_FILES =
            new FileChooser.ExtensionFilter("All Files", "*.*");

    public static File createNewFile(String filePath) {
        try {
            File newFolder = new File(filePath);
            var ok = newFolder.createNewFile();
            if (!ok) {
                String errorMessage = "No se puede crear un nuevo archivo en esta ruta. Verifique que no exista.";
                DialogUtils.createWarningDialog(DialogUtils.WARNING_DIALOG, null, errorMessage);
            }
            return newFolder;
        } catch (IOException e) {
            String errorMessage = "No se puede crear un nuevo archivo en esta ruta";
            DialogUtils.createErrorDialog(DialogUtils.ERROR_DIALOG, null, errorMessage);
            return null;
        }
    }

    public static File openSourceFile(String title) {
        FileChooser fileChooser = getNewFileChooser(title);
        return fileChooser.showOpenDialog(null);
    }

    public static File createNewFolder(String filePath) {
        File newFolder = new File(filePath);
        boolean ok = newFolder.mkdir();
        if (!ok) {
            String errorMessage = "Puede que el directorio ya exista.";
            DialogUtils.createWarningDialog(DialogUtils.WARNING_DIALOG, null, errorMessage);
        }
        return newFolder;
    }

    public static File createNewFileAndSave(String dirPath) {
        File directory = new File(dirPath);
        FileChooser fileChooser = getNewFileChooser("Nuevo archivo");
        fileChooser.setInitialDirectory(directory);
        return fileChooser.showSaveDialog(null);
    }

    public static File createNewFileAndSave() {
        FileChooser fileChooser = getNewFileChooser("Nuevo archivo");
        return fileChooser.showSaveDialog(null);
    }

    private static FileChooser getNewFileChooser(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(RMB_FILES);
        fileChooser.getExtensionFilters().add(ALL_FILES);
        return fileChooser;
    }

    public static void updateContent(File file, String content) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException iox) {
            String errorMessage = "No se puede guardar este archivo, asegúrese de que este archivo no se elimine";
            DialogUtils.createErrorDialog(DialogUtils.ERROR_DIALOG,null,errorMessage);
        }
    }

    public static void deleteFile(String filename) {
        File file = new File(filename);
        file.deleteOnExit();
    }

    public static File openSourceDir(String title) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle(title);
        return chooser.showDialog(null);
    }
}
