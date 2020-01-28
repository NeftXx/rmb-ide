package com.neftxx.ast.gui;

import com.neftxx.ast.expression.array.RmbArray;
import javafx.scene.control.Button;

public class RmbButton extends RmbComponent {
    public Button button;

    public RmbButton() {
        button = new Button();
    }

    @Override
    public void setText(String text) {
        button.setText(text);
    }

    @Override
    public void setWidth(int width) {
        button.setMinWidth(width);
    }

    @Override
    public void setHeight(int height) {
        button.setMinHeight(height);
    }

    @Override
    public void setPosition(int x, int y) {
        button.setLayoutX(x);
        button.setLayoutY(y);
    }

    @Override
    public String getText() {
        return button.getText();
    }

    @Override
    public int getWidth() {
        return (int) button.getWidth();
    }

    @Override
    public int getHeight() {
        return (int) button.getHeight();
    }

    @Override
    public RmbArray getPosition() {
        return getRmbArray(button.getLayoutX(), button.getLayoutY());
    }
}
