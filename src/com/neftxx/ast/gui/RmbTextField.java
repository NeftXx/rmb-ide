package com.neftxx.ast.gui;

import com.neftxx.ast.expression.array.RmbArray;
import javafx.scene.control.TextField;


public class RmbTextField extends RmbComponent {
    public TextField textField;

    public RmbTextField() {
        textField = new TextField();
    }

    @Override
    public void setText(String text) {
        textField.setText(text);
    }

    @Override
    public void setWidth(int width) {
        textField.setMinWidth(width);
    }

    @Override
    public void setHeight(int height) {
        textField.setMinHeight(height);
    }

    @Override
    public void setPosition(int x, int y) {
        textField.setLayoutX(x);
        textField.setLayoutY(y);
    }

    @Override
    public String getText() {
        return textField.getText();
    }

    @Override
    public int getWidth() {
        return (int) textField.getWidth();
    }

    @Override
    public int getHeight() {
        return (int) textField.getHeight();
    }

    @Override
    public RmbArray getPosition() {
        return getRmbArray(textField.getLayoutX(), textField.getLayoutY());
    }

}
