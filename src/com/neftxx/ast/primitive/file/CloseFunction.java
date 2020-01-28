package com.neftxx.ast.primitive.file;

import com.neftxx.ast.AstNode;
import com.neftxx.scope.Scope;
import com.neftxx.util.NodeInfo;

import java.io.IOException;

public class CloseFunction extends AstNode {
    public CloseFunction(NodeInfo info) {
        super(info);
    }

    @Override
    public Object interpret(Scope scope) {
        if (WRITE_FILE != null) {
            try {
                WRITE_FILE.close();
            } catch (IOException e) {
                reportError(e.getMessage());
            }
        }
        ITS_OPEN = false;
        WRITE_FILE = null;
        return null;
    }
}
