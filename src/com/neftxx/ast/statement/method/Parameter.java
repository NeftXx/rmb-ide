package com.neftxx.ast.statement.method;

import com.neftxx.ast.AstNode;
import com.neftxx.type.ArrayType;
import com.neftxx.type.RmbType;
import com.neftxx.util.NodeInfo;
import com.neftxx.scope.Scope;

public class Parameter extends AstNode {
    public int numberDim;
    public RmbType type;
    public final String id;

    public Parameter(NodeInfo info, RmbType type, String id) {
        super(info);
        this.type = type;
        this.id = id;
        numberDim = 0;
    }

    public void createType() {
        if (this.numberDim > 0) {
            this.type = new ArrayType(this.numberDim, type);
        }
    }

    @Override
    public Object interpret(Scope scope) {
        return null;
    }
}
