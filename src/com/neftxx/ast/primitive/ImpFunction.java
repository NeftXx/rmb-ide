package com.neftxx.ast.primitive;

import com.neftxx.ast.AstNode;
import com.neftxx.ast.expression.Expression;
import com.neftxx.ast.util.StringAnalyzer;
import com.neftxx.scope.Scope;
import com.neftxx.util.Console;
import com.neftxx.util.NodeInfo;

import java.util.ArrayList;

public class ImpFunction extends AstNode {
    public final ArrayList<Expression> expressions;

    public ImpFunction(NodeInfo info, ArrayList<Expression> expressions) {
        super(info);
        this.expressions = expressions;
    }

    @Override
    public Object interpret(Scope scope) {
        String val = StringAnalyzer.getCad(this, expressions, scope);
        if (val != null) {
            Console.append(val);
        }
        return null;
    }
}
