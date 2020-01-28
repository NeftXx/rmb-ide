package com.neftxx.ast.primitive;

import com.neftxx.ast.AstNode;
import com.neftxx.ast.statement.Block;
import com.neftxx.scope.Scope;
import com.neftxx.util.NodeInfo;

public class AlDarClickFunction extends AstNode {
    public Block block;

    public AlDarClickFunction(NodeInfo info, Block block) {
        super(info);
        this.block = block;
    }

    @Override
    public Object interpret(Scope scope) {
        block.interpret(scope);
        return null;
    }
}
