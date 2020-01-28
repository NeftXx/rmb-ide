package com.neftxx;

import com.neftxx.constant.Rmb;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;

/**
 * Archivo principal de la aplicación
 */
public class App extends Application {

    public static Stage mainStage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("view/main.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, Rmb.APP_WIDTH, Rmb.APP_HEIGHT);
        primaryStage.getIcons().add(Rmb.APP_ICON);
        primaryStage.setTitle(Rmb.APP_NAME);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        primaryStage.setMaximized(true);
        primaryStage.show();
        mainStage = primaryStage;
    }

    public static void main(String[] args) {
        launch();
    }

}