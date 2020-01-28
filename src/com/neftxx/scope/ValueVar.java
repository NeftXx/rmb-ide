package com.neftxx.scope;

public class ValueVar {
    public Object value;

    public ValueVar(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        if (value != null) {
            return value.toString();
        }
        return "null";
    }
}
