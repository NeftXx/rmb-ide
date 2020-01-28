package com.neftxx.ast.primitive;

import com.neftxx.ast.AstNode;
import com.neftxx.ast.expression.Expression;
import com.neftxx.ast.expression.array.ArrayNode;
import com.neftxx.ast.expression.array.RmbArray;
import com.neftxx.scope.Scope;
import com.neftxx.type.ArrayType;
import com.neftxx.type.PrimitiveType;
import com.neftxx.util.NodeInfo;

public class ConcFunction extends AstNode {
    public final Expression exp1;
    public final Expression exp2;

    public ConcFunction(NodeInfo info, Expression exp1, Expression exp2) {
        super(info);
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    @Override
    public Object interpret(Scope scope) {
        var valExp1 = exp1.interpret(scope);
        var valExp2 = exp2.interpret(scope);
        ArrayType typeTemp = new ArrayType(1, PrimitiveType.CHR);
        if (typeTemp.isAssignable(exp1.type)) {
            if (typeTemp.isAssignable(exp2.type)) {
                if (valExp1 instanceof RmbArray) {
                    if (valExp2 instanceof RmbArray) {
                        ((RmbArray) valExp1).concat(((RmbArray) valExp2).root);
                    } else if (valExp2 instanceof ArrayNode) {
                        ((RmbArray) valExp1).concat((ArrayNode) valExp2);
                    } else {
                        reportError("Error al concatenar");
                    }
                } else if (valExp1 instanceof ArrayNode) {
                    if (valExp2 instanceof RmbArray) {
                        ((ArrayNode) valExp1).concat(((RmbArray) valExp2).root);
                    } else if (valExp2 instanceof ArrayNode) {
                        ((ArrayNode) valExp1).concat((ArrayNode) valExp2);
                    } else {
                        reportError("Error al concatenar");
                    }
                } else {
                    reportError("Error al concatenar");
                }
            } else {
                reportError("Los tipos son incompatibles: " + exp2.type + " no se puede convertir en " + typeTemp);
            }
        } else {
            reportError("Los tipos son incompatibles: " + exp1.type + " no se puede convertir en " + typeTemp);
        }
        return null;
    }
}
