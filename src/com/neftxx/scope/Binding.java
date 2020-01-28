package com.neftxx.scope;

import com.neftxx.type.RmbType;

public class Binding {
    public String id;
    public RmbType type;
    public ValueVar valueVar;
    public boolean isConstant;

    public Binding(String id, RmbType type, Object value, boolean isConstant) {
        this.id = id;
        this.type = type;
        this.valueVar = new ValueVar(value);
        this.isConstant = isConstant;
    }

    @Override
    public String toString() {
        return "Binding{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", valueVar=" + valueVar +
                ", isConstant=" + isConstant +
                '}';
    }
}
