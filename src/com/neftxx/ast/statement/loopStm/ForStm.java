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

public class ForStm extends AstNode {
    public AstNode instruction;
    public Expression condition;
    public AstNode count;
    public Block block;

    public ForStm(NodeInfo info, AstNode instruction, Expression condition, AstNode count, Block block) {
        super(info);
        this.instruction = instruction;
        this.condition = condition;
        this.count = count;
        this.block = block;
    }

    @Override
    public Object interpret(Scope scope) {
        try {
            Scope myScope = new Scope(scope);
            myScope.setGlobal(scope.getGlobal());
            instruction.interpret(myScope);
            Object valCond = condition.interpret(myScope);
            var bulType = PrimitiveType.BUL;
            if (bulType.isAssignable(condition.type)) {
                valCond = Convert.toBoolean(condition.type, valCond);
                if (block != null) {
                    while ((boolean) valCond) {
                        var localScope = new Scope(myScope);
                        localScope.setGlobal(myScope.getGlobal());
                        Object val = block.interpret(localScope);
                        if (val instanceof ReturnStm) return val;
                        if (val instanceof BreakStm) return null;
                        count.interpret(myScope);
                        valCond = condition.interpret(myScope);
                        if (bulType.isAssignable(condition.type)) {
                            valCond = Convert.toBoolean(condition.type, valCond);
                        } else {
                            reportError("Lost tipos son incompatibles " + condition.type + " no se puede convertir a bul.");
                            return null;
                        }
                    }
                }
                return null;
            }
            reportError("Lost tipos son incompatibles " + condition.type + " no se puede convertir a bul.");
        } catch (RmbException e) {
            reportError(e.getMessage());
        }
        return null;
    }
}
