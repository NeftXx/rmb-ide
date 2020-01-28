package com.neftxx.ast.expression.operation;

import com.neftxx.ast.util.RmbException;
import com.neftxx.util.NodeInfo;
import com.neftxx.ast.expression.Expression;
import com.neftxx.scope.Scope;
import com.neftxx.type.PrimitiveType;
import com.neftxx.type.TypeTool;
import com.neftxx.ast.util.Convert;

public class Pow extends Operation {
    public Pow(NodeInfo info, Expression exp1, Expression exp2) {
        super(info, exp1, exp2);
    }

    @Override
    public Object calculateValue(Scope scope) throws RmbException {
        var val1 = this.exp1.interpret(scope);
        var val2 = this.exp2.interpret(scope);
        var maxType = TypeTool.max(this.exp1.type, this.exp2.type);

        if (TypeTool.isNumeric(maxType)) {
            this.type = PrimitiveType.ENT;
            double base = Convert.toDouble(this.exp1.type, val1);
            double exponent = Convert.toDouble(this.exp2.type, val2);
            double result = Math.pow(base, exponent);
            this.value = (int) result;
            return this.value;
        }

        return null;
    }
}
