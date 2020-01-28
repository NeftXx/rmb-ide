package com.neftxx.ast.expression.operation;

import com.neftxx.ast.util.RmbException;
import com.neftxx.ast.util.Convert;
import com.neftxx.util.NodeInfo;
import com.neftxx.ast.expression.Expression;
import com.neftxx.scope.Scope;
import com.neftxx.type.PrimitiveType;

public class Not extends Expression {
    public Expression expression;

    public Not(NodeInfo info, Expression expression) {
        super(info);
        this.expression = expression;
    }

    @Override
    public Object calculateValue(Scope scope) throws RmbException {
        var val = this.expression.interpret(scope);
        this.type = PrimitiveType.BUL;
        boolean res = Convert.toBoolean(this.expression.type, val);
        this.value = !res;
        return !res;
    }
}
