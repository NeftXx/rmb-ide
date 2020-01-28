package com.neftxx.ast.statement.fusion;

import com.neftxx.ast.AstNode;
import com.neftxx.ast.statement.field.VarDeclaration;
import com.neftxx.util.NodeInfo;
import com.neftxx.scope.Scope;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Fusion extends AstNode {
    public final String id;
    public final ArrayList<VarDeclaration> declarations;

    public Fusion(NodeInfo info, String id, ArrayList<VarDeclaration> declarations) {
        super(info);
        this.id = id;
        this.declarations = declarations;
    }

    public boolean verifyVariableNames() {
        String currentId = "";
        var thereIsError = new AtomicBoolean(false);
        for (var declaration: declarations) {
            if (currentId.equals(declaration.id)) {
                reportError("La variable " + currentId + " ya ha sido declarada en Fusion " + id);
                thereIsError.set(true);
            }
        }
        return thereIsError.get();
    }

    public int getSize() {
        return declarations.size();
    }

    @Override
    public Scope interpret(Scope scope) {
        Scope myScope = new Scope(scope.getGlobal());
        myScope.setGlobal(scope.getGlobal());
        for (var declaration: declarations) {
            declaration.interpret(myScope);
        }
        return myScope;
    }
}
