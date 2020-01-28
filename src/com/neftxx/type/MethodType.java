package com.neftxx.type;

import java.util.ArrayList;

public class MethodType extends RmbType {
    public final RmbType type;
    public final ArrayList<RmbType> params;

    public MethodType(String id, RmbType type, ArrayList<RmbType> params) {
        super(id, 1);
        this.type = type;
        this.params = params;
    }

    @Override
    public boolean isSame(RmbType other) {
        if (TypeTool.isMethod(other)) {
            MethodType methodType = (MethodType) other;
            return isSame(methodType.name, methodType.params);
        }
        return false;
    }

    @Override
    public boolean isAssignable(RmbType other) {
        return this == other;
    }

    public boolean isSame(String name, ArrayList<RmbType> params) {
        if (name.equals(this.name) && params.size() == this.params.size()) {
            int i = 0;
            for (; i < params.size(); i++) {
                if (!(params.get(i).isSame(this.params.get(i))))
                    return false;
            }
            return true;
        }
        return false;
    }

    public boolean isAssignable(String name, ArrayList<RmbType> params) {
        if (name.equals(this.name) && params.size() == this.params.size()) {
            int i = 0;
            for (; i < params.size(); i++) {
                if (!(params.get(i).isAssignable(this.params.get(i))))
                    return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return type + " " + name + params.toString();
    }
}
