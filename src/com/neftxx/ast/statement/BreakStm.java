package com.neftxx.ast.statement;

import com.neftxx.ast.AstNode;
import com.neftxx.util.NodeInfo;
import com.neftxx.scope.Scope;

public class BreakStm extends AstNode {
    public BreakStm(NodeInfo nodeInfo) {
        super(nodeInfo);
    }

    @Override
    public Object interpret(Scope scope) {
        return this;
    }
}
