package com.neftxx.ast.expression;

import com.neftxx.scope.Scope;
import com.neftxx.type.RmbType;
import com.neftxx.util.NodeInfo;

public class Literal extends Expression {

    public Literal(NodeInfo info, RmbType type, Object value) {
        super(info);
        this.value = value;
        this.type = type;
    }

    @Override
    protected Object calculateValue(Scope scope) {
        return value;
    }
}
