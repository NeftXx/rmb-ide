package com.neftxx.ast.expression.array;

import com.neftxx.type.RmbType;

public abstract class ArrayNodeComponent {
    public int numberDim = 0;
    public RmbType type;

    public int getNumberDim() {
        return numberDim;
    }

    public abstract boolean verifyTypes(RmbType type, StringBuilder builder);
    public abstract boolean verifyDimension(int numberDim, StringBuilder builder);
    public abstract boolean isValueNode();
    public abstract ArrayNodeComponent getNode(int position, int[] sizeDimensions) throws IndexOutOfBoundsException;
    public abstract RmbType getTypeValueNode();
}
