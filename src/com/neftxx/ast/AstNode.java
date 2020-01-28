package com.neftxx.ast;

import com.neftxx.error.ErrorHandler;
import com.neftxx.scope.Scope;
import com.neftxx.util.NodeInfo;
import org.apache.commons.io.FilenameUtils;

import java.io.BufferedWriter;

public abstract class AstNode {
    public static boolean ITS_OPEN = false;
    public static BufferedWriter WRITE_FILE = null;
    public final NodeInfo info;

    public AstNode(NodeInfo info) {
        this.info = info;
    }

    public abstract Object interpret(Scope scope);

    public void reportError(String description) {
        ErrorHandler.addSemanticError(description, this.info.line, this.info.column, FilenameUtils.getName(info.filename));
    }
}
