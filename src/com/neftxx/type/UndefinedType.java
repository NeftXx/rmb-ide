package com.neftxx.type;

public class UndefinedType extends RmbType {
    public static final UndefinedType UNDEFINED = new UndefinedType("undefined");

    private UndefinedType(String name) {
        super(name, -1);
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
