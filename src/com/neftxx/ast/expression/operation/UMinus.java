package com.neftxx.ast.expression.operation;

import com.neftxx.ast.util.RmbException;
import com.neftxx.ast.expression.Expression;
import com.neftxx.scope.Scope;
import com.neftxx.type.TypeTool;
import com.neftxx.ast.util.Convert;
import com.neftxx.type.UndefinedType;
import com.neftxx.util.NodeInfo;

public class UMinus extends Expression {
    public final Expression expression;

    public UMinus(NodeInfo info, Expression expression) {
        super(info);
        this.expression = expression;
    }

    @Override
    protected Object calculateValue(Scope scope) throws RmbException {
        Object val = expression.interpret(scope);
        type = expression.type;

        if (TypeTool.isEnt(type) || TypeTool.isChr(type)) {
            this.value = -(Convert.toInt(type, val));
            return this.value;
        }

        if (TypeTool.isDec(type)) {
            this.value = -(Convert.toDouble(type, val));
            return this.value;
        }
        this.type = UndefinedType.UNDEFINED;
        return null;
    }
}
