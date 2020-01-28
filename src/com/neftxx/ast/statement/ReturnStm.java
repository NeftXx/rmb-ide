package com.neftxx.ast.statement;

import com.neftxx.ast.AstNode;
import com.neftxx.type.VoidType;
import com.neftxx.util.NodeInfo;
import com.neftxx.ast.expression.Expression;
import com.neftxx.scope.Scope;
import com.neftxx.type.RmbType;

public class ReturnStm extends AstNode {
    public Expression expression;
    public Object value;
    public RmbType type;

    public ReturnStm(NodeInfo info) {
        super(info);
    }

    public ReturnStm(NodeInfo info, Expression expression) {
        super(info);
        this.expression = expression;
    }

    @Override
    public Object interpret(Scope scope) {
        if (expression != null) {
            value = expression.interpret(scope);
            type = expression.type;
        } else {
            value = null;
            type = VoidType.ZRO;
        }
        return this;
    }
}
