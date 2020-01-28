package com.neftxx.ast.gui;

import com.neftxx.ast.expression.array.RmbArray;
import javafx.scene.control.PasswordField;

public class RmbPasswordField extends RmbComponent {
    public PasswordField passwordField;

    public RmbPasswordField() {
        passwordField = new PasswordField();
    }

    @Override
    public void setText(String text) {
        passwordField.setText(text);
    }

    @Override
    public void setWidth(int width) {
        passwordField.setMinWidth(width);
    }

    @Override
    public void setHeight(int height) {
        passwordField.setMinHeight(height);
    }

    @Override
    public void setPosition(int x, int y) {
        passwordField.setLayoutX(x);
        passwordField.setLayoutY(y);
    }

    @Override
    public String getText() {
        return passwordField.getText();
    }

    @Override
    public int getWidth() {
        return (int) passwordField.getWidth();
    }

    @Override
    public int getHeight() {
        return (int) passwordField.getHeight();
    }

    @Override
    public RmbArray getPosition() {
        return getRmbArray(passwordField.getLayoutX(), passwordField.getLayoutY());
    }
}
