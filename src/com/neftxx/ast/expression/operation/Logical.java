package com.neftxx.ast.expression.operation;

import com.neftxx.ast.util.RmbException;
import com.neftxx.ast.util.Convert;
import com.neftxx.type.PrimitiveType;
import com.neftxx.util.NodeInfo;
import com.neftxx.ast.expression.Expression;
import com.neftxx.scope.Scope;
import com.neftxx.type.TypeTool;
import com.neftxx.type.UndefinedType;

public class Logical extends Operation {
    public enum Operator {
        OR, AND
    }

    private final Operator operator;

    public Logical(NodeInfo info, Expression exp1, Expression exp2, Operator operator) {
        super(info, exp1, exp2);
        this.operator = operator;
    }

    @Override
    protected Object calculateValue(Scope scope) throws RmbException {
        var val1 = this.exp1.interpret(scope);
        if (TypeTool.isUndefined(this.exp1.type)) {
            this.type = UndefinedType.UNDEFINED;
            return null;
        }
        boolean result1 = Convert.toBoolean(this.exp1.type, val1);
        if (this.operator == Operator.OR) {
            if (result1) {
                this.type = PrimitiveType.BUL;
                this.value = true;
                return true;
            }
            return getValExp2(scope);
        }
        if (result1) {
            return getValExp2(scope);
        }
        this.type = PrimitiveType.BUL;
        this.value = false;
        return false;
    }

    private Object getValExp2(Scope scope) throws RmbException {
        var val2 = this.exp2.interpret(scope);
        if (TypeTool.isUndefined(this.exp2.type)) {
            this.type = UndefinedType.UNDEFINED;
            return null;
        }
        this.value = Convert.toBoolean(this.exp2.type, val2);
        this.type = PrimitiveType.BUL;
        return this.value;
    }
}
