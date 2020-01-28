package com.neftxx.ast.expression.array;

import com.neftxx.ast.expression.Expression;
import com.neftxx.scope.Scope;
import com.neftxx.type.ArrayType;
import com.neftxx.type.PrimitiveType;
import com.neftxx.util.NodeInfo;

public class CharacterArray extends Expression {
    public String text;

    public CharacterArray(NodeInfo info, String text) {
        super(info);
        this.text = text;
    }

    public ArrayNode getArray() {
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

    @Override
    protected ArrayNode calculateValue(Scope scope) {
        return getArray();
    }
}
