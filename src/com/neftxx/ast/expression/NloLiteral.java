package com.neftxx.ast.expression;

import com.neftxx.scope.Scope;
import com.neftxx.type.NullType;
import com.neftxx.util.NodeInfo;

public class NloLiteral extends Expression {

    private static final NloLiteral instance = new NloLiteral(new NodeInfo(0, 0, ""));

    private NloLiteral(NodeInfo info) {
        super(info);
        this.type = NullType.NLO;
    }

    public static NloLiteral getInstance() {
        return instance;
    }

    @Override
    protected Object calculateValue(Scope scope) {
        return null;
    }
}
