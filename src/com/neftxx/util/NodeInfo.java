package com.neftxx.util;

public class NodeInfo {
    public final int line;
    public final int column;
    public final String filename;

    public NodeInfo(int line, int column, String filename) {
        this.line = line;
        this.column = column;
        this.filename = filename;
    }
}
