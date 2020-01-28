package com.neftxx.ast.expression;

import com.neftxx.scope.Scope;
import com.neftxx.util.NodeInfo;

public class Identifier extends Expression {
    public final String id;

    public Identifier(NodeInfo info, String id) {
        super(info);
        this.id = id;
    }

    @Override
    protected Object calculateValue(Scope scope) {
        var binding = scope.getVariable(id);
        if (binding != null) {
            this.type = binding.type;
            value = binding.valueVar.value;
            return value;
        }
        reportError("No se ha declarado la variable " + id + " en este entorno.");
        return null;
    }
}
