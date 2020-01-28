package com.neftxx.type;

public class NullType extends RmbType {
    public static final NullType NLO = new NullType("nlo", 0);
    public static final NullType RESERVA = new NullType("reserva", 0);
    public static final NullType REFERENCE = new NullType("referencia", 0);

    private NullType(String name, int size) {
        super(name, size);
    }

    @Override
    public boolean isSame(RmbType other) {
        return false;
    }

    @Override
    public boolean isAssignable(RmbType other) {
        return false;
    }

    @Override
    public String toString() {
        return name;
    }
}
