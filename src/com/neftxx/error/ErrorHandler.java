package com.neftxx.error;

import java.util.ArrayList;

public class ErrorHandler {
    private static final ArrayList<NodeError> errors = new ArrayList<>();

    public static ArrayList<NodeError> getErrors() {
        return errors;
    }

    public static void addLexicalError(String description, int line, int column, String filename) {
        errors.add(new NodeError(TypeError.LEXICAL, description, line, column, filename));
    }

    public static void addSyntacticError(String description, int line, int column, String filename) {
        errors.add(new NodeError(TypeError.SYNTACTIC, description, line, column, filename));
    }

    public static void addSemanticError(String description, int line, int column, String filename) {
        errors.add(new NodeError(TypeError.SEMANTIC, description, line, column, filename));
    }
}
