package com.neftxx.ast.primitive;

import com.neftxx.ast.expression.Expression;
import com.neftxx.ast.expression.array.ArrayNode;
import com.neftxx.ast.expression.array.RmbArray;
import com.neftxx.ast.util.RmbException;
import com.neftxx.ast.util.StringAnalyzer;
import com.neftxx.scope.Scope;
import com.neftxx.type.ArrayType;
import com.neftxx.type.PrimitiveType;
import com.neftxx.type.UndefinedType;
import com.neftxx.util.NodeInfo;

public class EqlsFunction extends Expression {
    public final Expression exp1;
    public final Expression exp2;

    public EqlsFunction(NodeInfo info, Expression exp1, Expression exp2) {
        super(info);
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    @Override
    protected Object calculateValue(Scope scope) throws RmbException {
        ArrayType typeTemp = new ArrayType(1, PrimitiveType.CHR);
        var valExp1 = exp1.interpret(scope);
        var valExp2 = exp2.interpret(scope);
        if (typeTemp.isAssignable(exp1.type)) {
            if (typeTemp.isAssignable(exp2.type)) {
                String cad1;
                String cad2;
                this.type = PrimitiveType.BUL;
                if (valExp1 instanceof RmbArray) {
                    cad1 = StringAnalyzer.obtenerCadena(((RmbArray) valExp1).root);
                } else if (valExp1 instanceof ArrayNode) {
                    cad1 = StringAnalyzer.obtenerCadena((ArrayNode) valExp1);
                } else {
                    return false;
                }

                if (valExp2 instanceof RmbArray) {
                    cad2 = StringAnalyzer.obtenerCadena(((RmbArray) valExp2).root);
                    return cad1.equals(cad2);
                } else if (valExp2 instanceof ArrayNode) {
                    cad2 = StringAnalyzer.obtenerCadena((ArrayNode) valExp2);
                    return cad1.equals(cad2);
                }
                return false;
            } else {
                reportError("Los tipos son incompatibles: " + exp2.type + " no se puede convertir en " + typeTemp);
            }
        } else {
            reportError("Los tipos son incompatibles: " + exp1.type + " no se puede convertir en " + typeTemp);
        }
        this.type = UndefinedType.UNDEFINED;
        return null;
    }
}
