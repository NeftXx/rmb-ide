package com.neftxx.ast.gui;

import com.neftxx.ast.expression.array.RmbArray;
import javafx.scene.control.Label;

public class RmbLabel extends RmbComponent {
    public Label label;

    public RmbLabel() {
        this.label = new Label();
    }

    @Override
    public void setText(String text) {
        label.setText(text);
    }

    @Override
    public void setWidth(int width) {
        label.setMinWidth(width);
    }

    @Override
    public void setHeight(int height) {
        label.setMinHeight(height);
    }

    @Override
    public void setPosition(int x, int y) {
        label.setLayoutX(x);
        label.setLayoutY(y);
    }

    @Override
    public String getText() {
        return label.getText();
    }

    @Override
    public int getWidth() {
        return (int) label.getWidth();
    }

    @Override
    public int getHeight() {
        return (int) label.getHeight();
    }

    @Override
    public RmbArray getPosition() {
        return getRmbArray(label.getLayoutX(), label.getLayoutY());
    }
}
