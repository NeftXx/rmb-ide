package com.neftxx.ast.expression.operation;

import com.neftxx.ast.util.RmbException;
import com.neftxx.util.NodeInfo;
import com.neftxx.ast.expression.Expression;
import com.neftxx.scope.Scope;
import com.neftxx.type.TypeTool;
import com.neftxx.type.UndefinedType;
import com.neftxx.ast.util.Convert;

public class Arithmetic extends Operation {
    public enum Operator {
        ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION, MODULE
    }

    private final Operator operator;

    public Arithmetic(NodeInfo info, Expression exp1, Expression exp2, Operator operator) {
        super(info, exp1, exp2);
        this.operator = operator;
    }

    @Override
    protected Object calculateValue(Scope scope) throws RmbException {
        var val1 = this.exp1.interpret(scope);
        var val2 = this.exp2.interpret(scope);
        var type1 = this.exp1.type;
        var type2 = this.exp2.type;

        this.type = TypeTool.max(type1, type2);

        if (TypeTool.isEnt(this.type)) {
            int result1 = Convert.toInt(type1, val1);
            int result2 = Convert.toInt(type2, val2);
            if ((this.operator.equals(Operator.DIVISION) || this.operator.equals(Operator.MODULE)) && result2 == 0) {
                reportError("Error en al realizar la operacion numerica");
                this.value = UndefinedType.UNDEFINED;
                return null;
            }
            this.value = toInt(result1, result2);
            return this.value;
        }

        if (TypeTool.isDec(this.type)) {
            double result1 = Convert.toDouble(type1, val1);
            double result2 = Convert.toDouble(type2, val2);
            if ((this.operator.equals(Operator.DIVISION) || this.operator.equals(Operator.MODULE)) && result2 == 0) {
                reportError("Error en al realizar la operacion numerica");
                this.value = UndefinedType.UNDEFINED;
                return null;
            }
            this.value = toDouble(result1, result2);
            return this.value;
        }

        this.type = UndefinedType.UNDEFINED;
        return null;
    }

    private int toInt(int val1 , int val2) {
        return (int) calculate(val1, val2);
    }

    private double toDouble(double val1, double val2) {
        return calculate(val1, val2);
    }

    private double calculate(double val1, double val2) {
        switch (this.operator) {
            case ADDITION:
                return val1 + val2;
            case SUBTRACTION:
                return val1 - val2;
            case MULTIPLICATION:
                return val1 * val2;
            case DIVISION:
                return val1 / val2;
        }
        // MODULE
        return val1 % val2;
    }
}
