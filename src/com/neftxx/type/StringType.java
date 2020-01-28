package com.neftxx.type;

public class StringType extends RmbType {
    public final static StringType R_STRING = new StringType("RString");

    protected StringType(String name) {
        super(name, 1);
    }

    @Override
    public boolean isSame(RmbType other) {
        return this == other;
    }

    @Override
    public boolean isAssignable(RmbType other) {
        if (TypeTool.isNull(other)) return true;
        if (TypeTool.isArray(other)) {
            ArrayType arrayType = (ArrayType) other;
            return TypeTool.isChr(arrayType.type) && arrayType.numberDim == 1;
        }
        return isSame(other);
    }

    @Override
    public String toString() {
        return name;
    }
}
