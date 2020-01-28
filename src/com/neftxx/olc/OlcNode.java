package com.neftxx.olc;

import com.neftxx.error.ErrorHandler;
import com.neftxx.evaluator.Source;
import com.neftxx.util.NodeInfo;
import javafx.scene.control.TreeItem;

public abstract class OlcNode {
    public final NodeInfo info;

    public OlcNode(NodeInfo info) {
        this.info = info;
    }

    public abstract TreeItem<Source> getFilesForProject(String projectPath);

    public void reportError(String description) {
        ErrorHandler.addSemanticError(description, this.info.line, this.info.column, this.info.filename);
    }
}
