package com.neftxx.ast.expression.operation;

import com.neftxx.util.NodeInfo;
import com.neftxx.ast.expression.Expression;

public abstract class Operation extends Expression {
    public Expression exp1;
    public Expression exp2;

    public Operation(NodeInfo info, Expression exp1, Expression exp2) {
        super(info);
        this.exp1 = exp1;
        this.exp2 = exp2;
    }
}
