package com.neftxx.ast.expression.operation;

import com.neftxx.ast.util.RmbException;
import com.neftxx.util.NodeInfo;
import com.neftxx.ast.expression.Expression;
import com.neftxx.scope.Scope;
import com.neftxx.type.PrimitiveType;
import com.neftxx.type.TypeTool;
import com.neftxx.ast.util.Convert;

public class Relational extends Operation {
    public enum Operator {
        GREATER_THAN, LESS_THAN, GREATER_THAN_OR_EQUAL_TO, LESS_THAN_OR_EQUAL_TO
    }

    private final Operator operator;

    public Relational(NodeInfo info, Expression exp1, Expression exp2, Operator operator) {
        super(info, exp1, exp2);
        this.operator = operator;
    }

    @Override
    protected Object calculateValue(Scope scope) throws RmbException {
        var val1 = this.exp1.interpret(scope);
        var val2 = this.exp2.interpret(scope);
        var type1 = this.exp1.type;
        var type2 = this.exp2.type;

        var maxType = TypeTool.max(type1, type2);

        if (TypeTool.isNumeric(maxType)) {
            this.type = PrimitiveType.BUL;
            double result1 = Convert.toDouble(type1, val1);
            double result2 = Convert.toDouble(type2, val2);
            this.value = compare(result1, result2);
            return this.value;
        }
        return null;
    }

    private boolean compare(double val1, double val2) {
        switch (this.operator) {
            case GREATER_THAN:
                return val1 > val2;
            case LESS_THAN:
                return val1 < val2;
            case GREATER_THAN_OR_EQUAL_TO:
                return val1 >= val2;
        }
        // LESS_THAN_OR_EQUAL_TO
        return val1 <= val2;
    }

}
