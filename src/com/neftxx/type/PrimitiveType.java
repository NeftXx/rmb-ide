package com.neftxx.type;

public class PrimitiveType extends RmbType {
    public static final PrimitiveType DEC = new PrimitiveType("dec", 64);
    public static final PrimitiveType ENT = new PrimitiveType("ent", 32);
    public static final PrimitiveType CHR = new PrimitiveType("chr", 16);
    public static final PrimitiveType BUL = new PrimitiveType("bul", 8);

    private PrimitiveType(String name, int size) {
        super(name, size);
    }

    @Override
    public boolean isSame(RmbType other) {
        return this == other;
    }

    @Override
    public boolean isAssignable(RmbType other) {
        if (TypeTool.isUndefined(this) || TypeTool.isUndefined(other) || TypeTool.isNull(this) || TypeTool.isNull(other))
            return false;
        return (!TypeTool.isBul(this) || !TypeTool.isDec(other)) && (!TypeTool.isDec(this) || !TypeTool.isBul(other));
    }

    @Override
    public String toString() {
        return name;
    }
}
