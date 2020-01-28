package com.neftxx.ast.expression.array;

import com.neftxx.ast.expression.Expression;
import com.neftxx.scope.Binding;
import com.neftxx.type.ArrayType;
import com.neftxx.type.RmbType;
import com.neftxx.type.UndefinedType;
import com.neftxx.util.NodeInfo;
import com.neftxx.scope.Scope;

import java.util.ArrayList;

public class ArrayInitializer extends Expression {
    public final ArrayList<Expression> expressions;

    public ArrayInitializer(NodeInfo info, ArrayList<Expression> expressions) {
        super(info);
        this.expressions = expressions;
    }

    public RmbArray createArray(RmbType type, Scope scope) {
        StringBuilder builder = new StringBuilder();
        ArrayNode root = calculateValue(scope);
        int numberDim = root.getNumberDim();
        boolean isBalanced = root.verifyDimension(numberDim, builder);
        if (isBalanced) {
            boolean rightTypes = root.verifyTypes(type, builder);
            if (rightTypes) {
                this.type = root.type;
                RmbArray array = new RmbArray((ArrayType) root.type, root);
                this.value = array;
                return array;
            }
            String[] errors = builder.toString().split("\n");
            for(String error: errors) {
                reportError(error);
            }
        } else {
            String[] errors = builder.toString().split("\n");
            for(String error: errors) {
                reportError(error);
            }
        }
        return null;
    }

    @Override
    protected ArrayNode calculateValue(Scope scope) {
        ArrayNode root = new ArrayNode();
        Object val;
        for (var exp: expressions) {
            val = exp.interpret(scope);
            if (val instanceof ArrayNode) {
                root.childrenNodes.add((ArrayNode) val);
            } else if (val instanceof RmbArray) {
                root.childrenNodes.add(((RmbArray) val).root);
            } else {
                ValueNode valueNode = new ValueNode(exp.type, val);
                root.childrenNodes.add(valueNode);
            }
        }
        root.calculateDim();
        this.type = root.type;
        return root;
    }

    public  RmbArray calculateArray(Scope scope) {
        ArrayNode root = calculateValue(scope);
        int numberDim = root.getNumberDim();
        StringBuilder builder = new StringBuilder();
        boolean isBalanced = root.verifyDimension(numberDim, builder);
        if (isBalanced) {
            var tempType = root.getTypeValueNode();
            root.verifyTypes(tempType, builder);
            this.type = root.type;
            RmbArray array = new RmbArray((ArrayType) root.type, root);
            this.value = array;
            return array;
        } else {
            String[] errors = builder.toString().split("\n");
            for(String error: errors) {
                reportError(error);
            }
        }
        this.type = UndefinedType.UNDEFINED;
        return null;
    }
}
