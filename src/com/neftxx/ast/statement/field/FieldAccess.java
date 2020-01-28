package com.neftxx.ast.statement.field;

import com.neftxx.ast.AstNode;
import com.neftxx.ast.expression.Identifier;
import com.neftxx.scope.Scope;
import com.neftxx.type.TypeTool;
import com.neftxx.util.NodeInfo;

import java.util.ArrayList;

public class FieldAccess extends AstNode {
    public final ArrayList<AstNode> nodes;

    public FieldAccess(NodeInfo info, ArrayList<AstNode> nodes) {
        super(info);
        this.nodes = nodes;
    }

    public AstNode getLast() {
        return nodes.get(nodes.size() - 1);
    }

    @Override
    public Scope interpret(Scope scope) {
        Scope currentScope = scope;
        AstNode lastNode = getLast();
        for (var node: nodes) {
            if (node != lastNode) {
                if (node instanceof Identifier) {
                    Identifier identifier = (Identifier) node;
                    var val = identifier.interpret(currentScope);
                    if (TypeTool.isFusion(identifier.type)) {
                        currentScope = (Scope) val;
                    } else {
                        reportError("Error no es un objeto para poder acceder a una variable.");
                        return null;
                    }
                }
            }
        }
        return currentScope;
    }
}
