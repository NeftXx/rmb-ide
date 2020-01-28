package com.neftxx.ast.statement.field;

import com.neftxx.ast.expression.Expression;
import com.neftxx.ast.expression.array.ArrayInitializer;
import com.neftxx.ast.expression.array.ArrayNode;
import com.neftxx.ast.expression.array.RmbArray;
import com.neftxx.scope.Binding;
import com.neftxx.scope.Scope;
import com.neftxx.type.ArrayType;
import com.neftxx.type.TypeTool;
import com.neftxx.type.UndefinedType;
import com.neftxx.util.NodeInfo;

public class ConstantDeclaration extends FieldDeclaration {
    public ConstantDeclaration(NodeInfo info, String id, Expression expression) {
        super(info, UndefinedType.UNDEFINED, id);
        this.expression = expression;
    }

    @Override
    public Object interpret(Scope scope) {
        Object val;
        if (expression instanceof ArrayInitializer) {
            val = ((ArrayInitializer) expression).calculateArray(scope);
        } else {
            val = expression.interpret(scope);
        }
        var expType = expression.type;

        if (TypeTool.isReference(expType)) {
            Binding ref = (Binding) val;
            scope.addVariable(id, ref);
            return null;
        }

        if (!TypeTool.isUndefined(expType)) {
            if (val instanceof ArrayNode) {
                val = new RmbArray((ArrayType) expression.type, (ArrayNode) val);
            }
            boolean notAdd = !scope.addVariable(id, new Binding(id, expression.type, val, true));
            if (notAdd) {
                reportError("Error la variable " + id + " ya ha sido declarada en este entorno.");
            }
        } else {
            reportError("No se puedo crear la constante " + id);
        }
        return null;
    }
}
