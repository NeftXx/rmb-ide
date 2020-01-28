package com.neftxx.ast.statement;

import com.neftxx.ast.AstNode;
import com.neftxx.util.NodeInfo;
import com.neftxx.scope.Scope;

public class ContinueStm extends AstNode {
    public ContinueStm(NodeInfo info) {
        super(info);
    }

    @Override
    public Object interpret(Scope scope) {
        return this;
    }
}
