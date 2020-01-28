package com.neftxx.error;

public class NodeError {
    public TypeError typeError;
    public String description;
    public int line;
    public int column;
    public String filename;

    public NodeError(TypeError typeError, String description, int line, int column, String filename) {
        this.typeError = typeError;
        this.description = description;
        this.line = line;
        this.column = column;
        this.filename = filename;
    }

    public TypeError getTypeError() {
        return typeError;
    }

    public void setTypeError(TypeError typeError) {
        this.typeError = typeError;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
