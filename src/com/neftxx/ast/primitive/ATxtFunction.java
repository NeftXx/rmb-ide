package com.neftxx.ast.primitive;

import com.neftxx.ast.expression.Expression;
import com.neftxx.ast.expression.array.ArrayNode;
import com.neftxx.ast.expression.array.RmbArray;
import com.neftxx.ast.expression.array.ValueNode;
import com.neftxx.ast.util.Convert;
import com.neftxx.ast.util.RmbException;
import com.neftxx.scope.Scope;
import com.neftxx.type.ArrayType;
import com.neftxx.type.PrimitiveType;
import com.neftxx.type.TypeTool;
import com.neftxx.util.NodeInfo;

public class ATxtFunction extends Expression {
    public Expression expression;

    public ATxtFunction(NodeInfo info, Expression expression) {
        super(info);
        this.expression = expression;
    }

    @Override
    protected ArrayNode calculateValue(Scope scope) throws RmbException {
        var valueExp = expression.interpret(scope);
        String text;
        if (TypeTool.isEnt(expression.type)) {
            int val = Convert.toInt(expression.type, valueExp);
            text = Integer.toString(val);
        } else if (TypeTool.isDec(expression.type)) {
            double val = Convert.toDouble(expression.type, valueExp);
            text = Double.toString(val);
        } else if (TypeTool.isRString(expression.type)) {
            text = valueExp.toString();
        } else if (TypeTool.isArray(expression.type))  {
            var arrayType = (ArrayType) expression.type;
            if (TypeTool.isChr(arrayType.type) && arrayType.numberDim == 1) {
                if (valueExp instanceof RmbArray) {
                    return ((RmbArray) valueExp).root;
                } else if (valueExp instanceof ArrayNode) {
                    return (ArrayNode) valueExp;
                }
            }
            reportError("El tipo " + expression.type + " no se puede convertir en chr[].");
            return null;
        } else {
            reportError("El tipo " + expression.type + " no se puede convertir en chr[].");
            return null;
        }
        int i;
        char[] str = text.toCharArray();
        ArrayNode root = new ArrayNode();
        root.type = new ArrayType(1, PrimitiveType.CHR);
        for (i = 0; i < str.length; i++) {
            ValueNode child = new ValueNode(PrimitiveType.CHR, str[i]);
            root.childrenNodes.add(child);
        }
        this.type = root.type;
        this.value = root;
        root.numberDim = 1;
        return root;
    }

}
