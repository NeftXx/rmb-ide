package com.neftxx.ast.gui;

import com.neftxx.ast.expression.Expression;
import com.neftxx.ast.util.RmbException;
import com.neftxx.scope.Scope;
import com.neftxx.type.TypeTool;
import com.neftxx.util.NodeInfo;

import java.util.ArrayList;

public class AccessToGuiMethodExp extends Expression {
    public final Expression expression;
    public final String id;
    public final ArrayList<Expression> parameters;

    public AccessToGuiMethodExp(NodeInfo info, Expression expression, String id, ArrayList<Expression> parameters) {
        super(info);
        this.expression = expression;
        this.id = id;
        this.parameters = parameters;
    }

    @Override
    protected Object calculateValue(Scope scope) throws RmbException {
        var val = expression.interpret(scope);
        if (TypeTool.isComponentType(expression.type)) {
            if (val != null) {
                RmbComponent rmbComponent = (RmbComponent) val;
                var methodCall = new MethodCallGui(info, rmbComponent, id, parameters);
                methodCall.calculateValue(scope);
                this.type = methodCall.type;
                this.value = methodCall.value;
                return this.value;
            } else {
                reportError("Error al acceder al objeto ya que es null");
            }
        }
        reportError("No se puede acceder al metodo " + id + " del objeto " + expression.type);
        return null;
    }
}
