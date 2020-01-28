package com.neftxx.ast.expression;

import com.neftxx.scope.Binding;
import com.neftxx.scope.Scope;
import com.neftxx.type.NullType;
import com.neftxx.type.TypeTool;
import com.neftxx.util.NodeInfo;

public class Reference extends Expression {
    public final Expression expression;

    public Reference(NodeInfo info, Expression expression) {
        super(info);
        this.expression = expression;
    }

    @Override
    protected Binding calculateValue(Scope scope) {
        if (expression instanceof VarAccess) {
            VarAccess varAccess = (VarAccess) expression;
            String id = varAccess.getId();
            if (id != null) {
                var expAccess = varAccess.expression;
                var val = expAccess.interpret(scope);
                if (TypeTool.isFusion(expAccess.type)) {
                    Binding binding = (Binding) val;
                    if (binding != null) {
                        this.type = NullType.REFERENCE;
                        this.value = binding;
                        return binding;
                    } else {
                        reportError("La variable " + id + " no existe en el objeto " + expAccess.type);
                    }
                } else {
                    reportError("No se puede acceder a la variable " + id + " del objeto " + expAccess.type);
                }
            } else {
                reportError("Error al intentar obtener la referencia");
            }
        } else if (expression instanceof Identifier) {
            Identifier identifier = (Identifier) expression;
            var binding = scope.getVariable(identifier.id);
            if (binding != null) {
                this.type = NullType.REFERENCE;
                this.value = binding;
                return binding;
            } else {
                reportError("No se ha declarado esta variable " + identifier.id);
            }
        } else {
            reportError("No se puede realizar la referencia a este tipo de expresion");
        }
        return null;
    }
}
