package com.neftxx.util;

public class SymbolNode {
    public String name;
    public String type;
    public String scope;
    public String rol;
    public String parameter;
    public String isConstant;

    public SymbolNode(String name, String type, String scope, String rol, String parameter, String isConstant) {
        this.name = name;
        this.type = type;
        this.scope = scope;
        this.rol = rol;
        this.parameter = parameter;
        this.isConstant = isConstant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getIsConstant() {
        return isConstant;
    }

    public void setIsConstant(String isConstant) {
        this.isConstant = isConstant;
    }
}
