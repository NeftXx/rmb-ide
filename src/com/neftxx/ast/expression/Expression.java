package com.neftxx.ast.expression;

import com.neftxx.ast.AstNode;
import com.neftxx.ast.util.RmbException;
import com.neftxx.scope.Scope;
import com.neftxx.util.NodeInfo;
import com.neftxx.type.RmbType;
import com.neftxx.type.UndefinedType;

public abstract class Expression extends AstNode {
    public RmbType type;
    public Object value;

    public Expression(NodeInfo info) {
        super(info);
        type = UndefinedType.UNDEFINED;
    }

    @Override
    public Object interpret(Scope scope) {
        try {
            return calculateValue(scope);
        } catch (RmbException e) {
            reportError(e.getMessage());
            return null;
        }
    }

    protected abstract Object calculateValue(Scope scope) throws RmbException;
}
