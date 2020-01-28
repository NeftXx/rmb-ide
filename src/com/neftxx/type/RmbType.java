package com.neftxx.type;

public abstract class RmbType {
    protected final String name;
    public final int size;

    protected RmbType(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public abstract boolean isSame(RmbType other);
    public abstract boolean isAssignable(RmbType other);
}
