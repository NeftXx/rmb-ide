package com.neftxx.util;

import com.neftxx.controller.MainController;

public class Console {
    public static final StringBuilder builder = new StringBuilder();
    public static void append(Object value) {
        try {
            builder.append(value.toString());
            MainController mainController = MainController.getInstance();
            mainController.consoleTextArea.setText(builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
