package com.neftxx.ast.statement;

import com.neftxx.ast.AstNode;
import com.neftxx.util.NodeInfo;
import com.neftxx.scope.Scope;

import java.util.ArrayList;

public class Block extends AstNode {
    public ArrayList<AstNode> astNodes;

    public Block(NodeInfo info) {
        super(info);
    }

    public Block(NodeInfo info, ArrayList<AstNode> astNodes) {
        super(info);
        this.astNodes = astNodes;
    }

    @Override
    public Object interpret(Scope scope) {
        if (astNodes != null) {
            Object value;
            for (AstNode astNode : astNodes) {
                value = astNode.interpret(scope);
                if (value instanceof BreakStm || value instanceof ContinueStm || value instanceof ReturnStm) {
                    return value;
                }
            }
        }
        return null;
    }
}
