package com.neftxx.ast.expression.operation;

import com.neftxx.ast.util.RmbException;
import com.neftxx.type.UndefinedType;
import com.neftxx.util.NodeInfo;
import com.neftxx.ast.expression.Expression;
import com.neftxx.scope.Scope;
import com.neftxx.type.PrimitiveType;
import com.neftxx.type.TypeTool;
import com.neftxx.ast.util.Convert;

public class Comparator extends Operation {
    public enum Operator {
        EQUAL_TO, NOT_EQUAL_TO
    }

    public final Operator operator;

    public Comparator(NodeInfo info, Expression exp1, Expression exp2, Operator operator) {
        super(info, exp1, exp2);
        this.operator = operator;
    }

    @Override
    protected Object calculateValue(Scope scope) throws RmbException {
        var val1 = this.exp1.interpret(scope);
        var val2 = this.exp2.interpret(scope);
        this.type = PrimitiveType.BUL;

        if (TypeTool.isPrimitiveType(this.exp1.type) && TypeTool.isPrimitiveType(this.exp2.type)) {
            var maxType = TypeTool.max(this.exp1.type, this.exp2.type);
            if (TypeTool.isNumeric(maxType)) {
                val1 = Convert.toDouble(this.exp1.type, val1);
                val2 = Convert.toDouble(this.exp2.type, val2);
            }

            if (this.operator.equals(Operator.EQUAL_TO)) {
                this.value = val1.equals(val2);
                return this.value;
            }

            // NOT_EQUAL_TO
            this.value = !(val1.equals(val2));
            return this.value;
        }

        if (TypeTool.isReference(exp1.type) && TypeTool.isReference(exp2.type)) {
            return this.operator.equals(Operator.EQUAL_TO) == (val1 == val2);
        }

        if (TypeTool.isFusion(exp1.type) && TypeTool.isFusion(exp2.type)) {
            return this.operator.equals(Operator.EQUAL_TO) == (val1 == val2);
        }

        if (TypeTool.isFusion(exp1.type) && TypeTool.isNull(exp2.type)) {
            return this.operator.equals(Operator.EQUAL_TO) == (val1 == null);
        }

        if (TypeTool.isNull(exp1.type) && TypeTool.isFusion(exp2.type)) {
            return this.operator.equals(Operator.EQUAL_TO) == (val2 == null);
        }

        this.type = UndefinedType.UNDEFINED;
        return null;
    }
}
