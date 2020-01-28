package com.neftxx.type;

public class ArrayType extends RmbType {
    public int numberDim;
    public RmbType type;

    public ArrayType(int numberDim, RmbType type) {
        super("array", 1);
        this.numberDim = numberDim;
        this.type = type;
    }

    @Override
    public boolean isSame(RmbType other) {
        if (!TypeTool.isArray(other)) {
            return false;
        }
        ArrayType arrayType = (ArrayType) other;
        if (this.numberDim != arrayType.numberDim) {
            return false;
        }
        return this.type.isSame(arrayType.type);
    }

    @Override
    public boolean isAssignable(RmbType other) {
        if (TypeTool.isNull(other)) {
            return true;
        }
        return isSame(other);
    }

    @Override
    public String toString() {
        int i = 0;
        StringBuilder s = new StringBuilder(type.toString());
        for (; i < numberDim; i++) {
            s.append("[]");
        }
        return s.toString();
    }
}
