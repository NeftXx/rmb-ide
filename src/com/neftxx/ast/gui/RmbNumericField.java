package com.neftxx.ast.gui;

import com.neftxx.ast.expression.array.RmbArray;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class RmbNumericField extends RmbComponent {

    public Spinner<Integer> spinner;

    public RmbNumericField() {
        spinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        spinner.setValueFactory(valueFactory);
    }

    @Override
    public void setText(String text) {
        try {
            int value = (int) Double.parseDouble(text);
            spinner.getValueFactory().setValue(value);
        } catch (NumberFormatException | ClassCastException e) {
            spinner.getValueFactory().setValue(0);
        }

    }

    @Override
    public void setWidth(int width) {
        spinner.setMinWidth(width);
    }

    @Override
    public void setHeight(int height) {
        spinner.setMinHeight(height);
    }

    @Override
    public void setPosition(int x, int y) {
        spinner.setLayoutX(x);
        spinner.setLayoutY(y);
    }

    @Override
    public String getText() {
        return spinner.getValue().toString();
    }

    @Override
    public int getWidth() {
        return (int) spinner.getWidth();
    }

    @Override
    public int getHeight() {
        return (int) spinner.getHeight();
    }

    @Override
    public RmbArray getPosition() {
        return getRmbArray(spinner.getLayoutX(), spinner.getLayoutY());
    }
}
