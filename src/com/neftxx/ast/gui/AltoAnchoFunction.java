package com.neftxx.ast.gui;

import com.neftxx.ast.AstNode;
import com.neftxx.ast.expression.Expression;
import com.neftxx.ast.util.Convert;
import com.neftxx.scope.Scope;
import com.neftxx.util.NodeInfo;

public class AltoAnchoFunction extends AstNode {
    public Expression expWidth;
    public Expression expHeight;
    public int width;
    public int height;

    public AltoAnchoFunction(NodeInfo info, Expression expHeight, Expression expWidth) {
        super(info);
        this.expWidth = expWidth;
        this.expHeight = expHeight;
        this.width = -1;
        this.height = -1;
    }

    @Override
    public Object interpret(Scope scope) {
        try {
            var valHeight = expHeight.interpret(scope);
            var valWidth = expWidth.interpret(scope);
            height = Convert.toInt(expHeight.type, valHeight);
            width = Convert.toInt(expWidth.type, valWidth);
        } catch (Exception e) {
            width = -1;
            height = -1;
            reportError(e.getMessage());
        }
        return null;
    }
}
