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
import com.neftxx.util.NodeInfo;

public class WhileStm extends AstNode {
    public final Expression expression;
    public final Block block;

    public WhileStm(NodeInfo info, Expression expression, Block block) {
        super(info);
        this.expression = expression;
        this.block = block;
    }

    @Override
    public Object interpret(Scope scope) {
        var val = expression.interpret(scope);
        var bul = PrimitiveType.BUL;

        try {
            if (bul.isAssignable(expression.type)) {
                val = Convert.toBoolean(expression.type, val);
                while ((boolean) val) {
                    Scope scopeWhile = new Scope(scope);
                    scopeWhile.setGlobal(scope.getGlobal());
                    var valBlock = block.interpret(scopeWhile);
                    if (valBlock instanceof BreakStm) return null;
                    if (valBlock instanceof ReturnStm) return valBlock;
                    val = expression.interpret(scope);
                    if (bul.isAssignable(expression.type)) {
                        val = Convert.toBoolean(expression.type, val);
                    } else {
                        reportError("Lost tipos son incompatibles " + expression.type + " no se puede convertir a bul.");
                        return null;
                    }
                }
                return null;
            }
        } catch (RmbException e) {
            reportError(e.getMessage());
        }
        reportError("Lost tipos son incompatibles " + expression.type + " no se puede convertir a bul.");
        return null;
    }
}
