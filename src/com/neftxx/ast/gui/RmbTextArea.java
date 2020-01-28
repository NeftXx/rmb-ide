package com.neftxx.ast.gui;

import com.neftxx.ast.expression.array.RmbArray;
import javafx.scene.control.TextArea;

public class RmbTextArea extends RmbComponent {
    public TextArea textArea;

    public RmbTextArea() {
        textArea = new TextArea();
    }

    @Override
    public void setText(String text) {
        textArea.setText(text);
    }

    @Override
    public void setWidth(int width) {
        textArea.setMinWidth(width);
    }

    @Override
    public void setHeight(int height) {
        textArea.setMinHeight(height);
    }

    @Override
    public void setPosition(int x, int y) {
        textArea.setLayoutX(x);
        textArea.setLayoutY(y);
    }

    @Override
    public String getText() {
        return textArea.getText();
    }

    @Override
    public int getWidth() {
        return (int) textArea.getWidth();
    }

    @Override
    public int getHeight() {
        return (int) textArea.getHeight();
    }

    @Override
    public RmbArray getPosition() {
        return getRmbArray(textArea.getLayoutX(), textArea.getLayoutY());
    }
}
