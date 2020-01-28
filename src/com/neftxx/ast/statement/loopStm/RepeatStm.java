package com.neftxx.ast.statement.loopStm;

import com.neftxx.ast.AstNode;
import com.neftxx.ast.expression.Expression;
import com.neftxx.ast.statement.Block;
import com.neftxx.ast.statement.BreakStm;
import com.neftxx.ast.statement.ReturnStm;
import com.neftxx.ast.util.Convert;
import com.neftxx.ast.util.RmbException;
import com.neftxx.scope.Scope;
import com.neftxx.type.PrimitiveType;
import com.neftxx.type.RmbType;
import com.neftxx.util.NodeInfo;

public class RepeatStm extends AstNode {
    public final Expression expression;
    public final Block block;

    public RepeatStm(NodeInfo info, Expression expression, Block block) {
        super(info);
        this.expression = expression;
        this.block = block;
    }

    @Override
    public Object interpret(Scope scope) {
        try {
            boolean cond = false;
            Object valBlock;
            Object valCond;
            RmbType type;
            var bulType = PrimitiveType.BUL;
            do {
                Scope localScope;
                localScope = new Scope(scope);
                localScope.setGlobal(scope.getGlobal());
                valBlock = block.interpret(localScope);
                if (valBlock instanceof BreakStm) return null;
                if (valBlock instanceof ReturnStm) return valBlock;
                valCond = expression.interpret(scope);
                type = expression.type;
                if (bulType.isAssignable(type)) {
                    cond = Convert.toBoolean(type, valCond);
                } else {
                    reportError("Los tipos son incompatibles:" + type + " no se puede convertir en bul.");
                    return null;
                }
            } while (cond);
        } catch (RmbException e) {
            reportError(e.getMessage());
        }
        return null;
    }
}
