package com.neftxx.ast.gui;

import com.neftxx.ast.expression.Expression;
import com.neftxx.ast.expression.array.ArrayNode;
import com.neftxx.ast.expression.array.RmbArray;
import com.neftxx.ast.expression.array.ValueNode;
import com.neftxx.ast.util.Convert;
import com.neftxx.ast.util.RmbException;
import com.neftxx.ast.util.StringAnalyzer;
import com.neftxx.scope.Scope;
import com.neftxx.type.*;
import com.neftxx.util.NodeInfo;

import java.util.ArrayList;

public abstract class RmbComponent {
    protected RmbArray getRmbArray(double layoutX, double layoutY) {
        ArrayNode root = new ArrayNode();
        ArrayType arrayType = new ArrayType(1, PrimitiveType.ENT);
        root.type = arrayType;
        root.childrenNodes.add(new ValueNode(PrimitiveType.ENT, (int) layoutX));
        root.childrenNodes.add(new ValueNode(PrimitiveType.ENT, (int) layoutY));
        root.numberDim = 1;
        return new RmbArray(arrayType, root);
    }

    public abstract void setText(String text);
    public abstract void setWidth(int width);
    public abstract void setHeight(int height);
    public abstract void setPosition(int x, int y);
    public abstract String getText();
    public abstract int getWidth();
    public abstract int getHeight();
    public abstract RmbArray getPosition();
}
