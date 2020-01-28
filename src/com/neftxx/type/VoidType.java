package com.neftxx.type;

public class VoidType extends RmbType {
    public static final VoidType ZRO = new VoidType("zro", 0);

    private VoidType(String name, int size) {
        super(name, size);
    }

    @Override
    public boolean isSame(RmbType other) {
        return this == other;
    }

    @Override
    public boolean isAssignable(RmbType other) {
        return this == other;
    }

    @Override
    public String toString() {
        return name;
    }
}
