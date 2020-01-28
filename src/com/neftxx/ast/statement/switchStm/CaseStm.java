package com.neftxx.ast.statement.switchStm;

import com.neftxx.ast.AstNode;
import com.neftxx.ast.expression.Expression;
import com.neftxx.ast.statement.BreakStm;
import com.neftxx.ast.statement.ContinueStm;
import com.neftxx.ast.statement.ReturnStm;
import com.neftxx.scope.Scope;
import com.neftxx.util.NodeInfo;

import java.util.ArrayList;

public class CaseStm extends AstNode {
    public Expression expression;
    public ArrayList<AstNode> astNodes;

    public CaseStm(NodeInfo info, Expression expression, ArrayList<AstNode> astNodes) {
        super(info);
        this.expression = expression;
        this.astNodes = astNodes;
    }

    @Override
    public Object interpret(Scope scope) {
        if (astNodes != null) {
            for (AstNode astNode: astNodes) {
                Object value = astNode.interpret(scope);
                if (value instanceof BreakStm || value instanceof ContinueStm || value instanceof ReturnStm)
                    return value;
            }
        }
        return null;
    }
}
