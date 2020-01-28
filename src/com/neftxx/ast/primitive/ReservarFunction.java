package com.neftxx.ast.primitive;

import com.neftxx.ast.expression.Expression;
import com.neftxx.ast.util.Convert;
import com.neftxx.ast.util.RmbException;
import com.neftxx.scope.Scope;
import com.neftxx.type.NullType;
import com.neftxx.util.NodeInfo;

public class ReservarFunction extends Expression {
    public final Expression expression;

    public ReservarFunction(NodeInfo info, Expression expression) {
        super(info);
        this.expression = expression;
    }

    @Override
    protected Object calculateValue(Scope scope) throws RmbException {
        var val = expression.interpret(scope);
        int res = Convert.toInt(expression.type, val);
        this.type = NullType.RESERVA;
        this.value = res;
        return res;
    }
}
