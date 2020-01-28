package com.neftxx.error;

public enum TypeError {
    LEXICAL("Léxico"),
    SYNTACTIC("Sintáctico"),
    SEMANTIC("Semántico");

    public String name;

    TypeError(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
