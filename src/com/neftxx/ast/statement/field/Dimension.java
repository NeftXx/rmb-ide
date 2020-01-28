package com.neftxx.ast.statement.field;

import com.neftxx.ast.expression.Expression;
import com.neftxx.ast.util.Convert;
import com.neftxx.ast.util.RmbException;
import com.neftxx.scope.Scope;

public class Dimension {
    public final Expression expression;

    public Dimension(Expression expression) {
        this.expression = expression;
    }

    public Dimension() {
        this(null);
    }

    public int calculateDim(Scope scope) throws RmbException {
        if (expression != null) {
            var val = expression.interpret(scope);
            return Convert.toInt(expression.type, val);
        }
        return -1;
    }
}
