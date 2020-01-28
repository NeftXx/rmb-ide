package com.neftxx.ast.expression.array;

import com.neftxx.type.ArrayType;

public class RmbArray {
    public static final int POSITION_INITIAL = 0;
    public ArrayType type;
    public final ArrayNode root;

    public RmbArray(ArrayType type, ArrayNode root) {
        this.type = type;
        this.root = root;
    }

    public boolean verifyNumberDimensions (int numberDim, StringBuilder builder) {
        return root.verifyDimension(numberDim, builder);
    }

    public boolean verifySizeOfDimensions(int[] indexes, StringBuilder builder) throws IndexOutOfBoundsException {
        return root.verifySizeOfDimensions(POSITION_INITIAL, indexes, builder);
    }

    public int getNumberDim() {
        root.calculateDim();
        return root.getNumberDim();
    }

    public void concat(ArrayNode arrayNode) {
        root.concat(arrayNode);
    }

    @Override
    public String toString() {
        if (root != null) {
            return root.toString();
        }
        return "";
    }
}
