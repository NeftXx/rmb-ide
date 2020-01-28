package com.neftxx.scope;

import java.util.Hashtable;

public class Scope {
    public Hashtable<String, Binding> variables;
    public Scope previous;
    private FileScope global;

    public Scope(Scope previous) {
        this.previous = previous;
        variables = new Hashtable<>();
        global = null;
    }

    public void setGlobal(FileScope global) {
        this.global = global;
    }

    public FileScope getGlobal() {
        return global;
    }

    public boolean addVariable(String id, Binding binding) {
        if (variables.containsKey(id)) return false;
        variables.put(id, binding);
        return true;
    }

    public void changeReference(String id, Binding binding) {
        variables.put(id, binding);
    }

    public Binding getVariable(String id) {
        for (Scope scope = this; scope != null; scope = scope.previous) {
            Binding found = scope.variables.get(id);
            if (found != null) return found;
        }
        return null;
    }

    public Binding getVariableLocal(String id) {
        return variables.get(id);
    }

    @Override
    public String toString() {
        return "Scope{" +
                "variables=" + variables +
                '}';
    }
}
