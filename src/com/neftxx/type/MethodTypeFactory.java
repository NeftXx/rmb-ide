package com.neftxx.type;

import com.neftxx.ast.statement.method.MethodStm;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class MethodTypeFactory {
    private final Hashtable<MethodType, MethodStm> methodTypes;

    public MethodTypeFactory() {
        this.methodTypes = new Hashtable<>();
    }

    public MethodStm getMethod(String id, ArrayList<RmbType> params) {
        Enumeration<MethodType> enumeration = methodTypes.keys();
        MethodType keyCurrent;
        boolean found;
        while (enumeration.hasMoreElements()) {
            keyCurrent = enumeration.nextElement();
            found = keyCurrent.isSame(id, params);
            if (found) return methodTypes.get(keyCurrent);
        }

        enumeration = methodTypes.keys();
        while (enumeration.hasMoreElements()) {
            keyCurrent = enumeration.nextElement();
            found = keyCurrent.isAssignable(id, params);
            if (found) return methodTypes.get(keyCurrent);
        }
        return null;
    }

    private boolean existMethod(String id, ArrayList<RmbType> params) {
        Enumeration<MethodType> enumeration = methodTypes.keys();
        MethodType keyCurrent;
        boolean found;
        while (enumeration.hasMoreElements()) {
            keyCurrent = enumeration.nextElement();
            found = keyCurrent.isSame(id, params);
            if (found) return true;
        }
        return false;
    }

    public boolean putMethodType(String id, RmbType type, ArrayList<RmbType> params, MethodStm method) {
        if (existMethod(id, params)) return false;
        methodTypes.put(new MethodType(id, type, params), method);
        return true;
    }
}
