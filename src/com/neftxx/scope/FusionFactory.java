package com.neftxx.scope;

import com.neftxx.ast.statement.fusion.Fusion;
import java.util.Hashtable;

public class FusionFactory {
    private final Hashtable<String, Fusion> fusions;

    public FusionFactory() {
        this.fusions = new Hashtable<>();
    }

    public Fusion getFusion(String name) {
        return this.fusions.get(name);
    }

    public boolean putFusion(String name, Fusion fusion) {
        if (this.fusions.containsKey(name)) return false;
        this.fusions.put(name, fusion);
        return true;
    }
}
