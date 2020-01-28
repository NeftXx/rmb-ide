package com.neftxx.ast.statement.ifStm;

import com.neftxx.ast.AstNode;
import com.neftxx.ast.expression.Expression;
import com.neftxx.ast.statement.Block;
import com.neftxx.ast.statement.BreakStm;
import com.neftxx.ast.statement.ContinueStm;
import com.neftxx.ast.statement.ReturnStm;
import com.neftxx.ast.util.Convert;
import com.neftxx.ast.util.RmbException;
import com.neftxx.scope.Scope;
import com.neftxx.type.PrimitiveType;
import com.neftxx.util.NodeInfo;

public class SubIfStm extends AstNode {
    public boolean condValue;
    public final boolean isElse;
    public Expression expression;
    public Block block;

    public SubIfStm(NodeInfo info, Expression expression, Block block) {
        super(info);
        this.expression = expression;
        this.block = block;
        isElse = false;
    }

    public SubIfStm(NodeInfo info, Block block) {
        super(info);
        expression = null;
        this.block = block;
        isElse = true;
    }

    public boolean getCondValue() {
        return condValue || isElse;
    }

    @Override
    public Object interpret(Scope scope) {
        try {
            Object val = false;
            if (expression != null) {
                val = expression.interpret(scope);
                var butType = PrimitiveType.BUL;
                if (butType.isAssignable(expression.type)) {
                    val = Convert.toBoolean(expression.type, val);
                } else {
                    reportError("Lost tipos son incompatibles " + expression.type + " no se puede convertir a bul.");
                    return null;
                }
            }
            condValue = (boolean) val;
            if (condValue || isElse) {
                Scope localScope = new Scope(scope);
                localScope.setGlobal(scope.getGlobal());
                Object res = block.interpret(localScope);
                if (res instanceof ReturnStm || res instanceof BreakStm || res instanceof ContinueStm)
                    return res;
            }
        } catch (RmbException e) {
            reportError(e.getMessage());
        }
        return null;
    }
}
