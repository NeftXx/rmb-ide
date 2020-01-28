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

public class ADecFunction extends Expression {
    public final Expression expression;

    public ADecFunction(NodeInfo info, Expression expression) {
        super(info);
        this.expression = expression;
    }

    @Override
    protected Object calculateValue(Scope scope) {
        ArrayType typeTemp = new ArrayType(1, PrimitiveType.CHR);
        var value = expression.interpret(scope);
        if (typeTemp.isAssignable(expression.type)) {
            try {
                String text = "0";
                if (value instanceof ArrayNode) {
                    text = StringAnalyzer.obtenerCadena((ArrayNode) value);
                } else if (value instanceof RmbArray) {
                    text = StringAnalyzer.obtenerCadena(((RmbArray) value).root);
                }
                this.type = PrimitiveType.DEC;
                return Double.parseDouble(text);
            } catch(NumberFormatException e) {
                reportError("Error la cadena no se puede convertir en numero decimal.");
            }
        } else {
            reportError("Los tipos son incompatibles: " + expression.type + " no se puede convertir en " + typeTemp);
        }
        this.type = UndefinedType.UNDEFINED;
        return null;
    }
}
