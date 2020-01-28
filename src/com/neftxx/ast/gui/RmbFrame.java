package com.neftxx.ast.gui;

import com.neftxx.App;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RmbFrame {
    public Stage stage;
    private Pane root;

    public RmbFrame() {
        stage = new Stage();
        stage.initOwner(App.mainStage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Rmb - Frame");
        root = new Pane();
        Scene scene = new Scene(root, 500, 500);
        stage.setScene(scene);
    }

    public void setSize(int width, int height) {
        stage.setWidth(width);
        stage.setHeight(height);
    }

    public void showAndWait() {
        stage.showAndWait();
    }

    public void addElement(Node node) {
        root.getChildren().add(node);
    }
}
