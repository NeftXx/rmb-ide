package com.neftxx.ast.primitive.file;

import com.neftxx.ast.AstNode;
import com.neftxx.ast.expression.Expression;
import com.neftxx.ast.util.StringAnalyzer;
import com.neftxx.scope.Scope;
import com.neftxx.util.NodeInfo;

import java.io.IOException;
import java.util.ArrayList;

public class WFFunction extends AstNode {
    public final ArrayList<Expression> expressions;

    public WFFunction(NodeInfo info, ArrayList<Expression> expressions) {
        super(info);
        this.expressions = expressions;
    }

    @Override
    public Object interpret(Scope scope) {
        if (WRITE_FILE != null && ITS_OPEN) {
            String val = StringAnalyzer.getCad(this, expressions, scope);
            if (val != null) {
                try {
                    WRITE_FILE.write(val);
                } catch (IOException e) {
                    reportError(e.getMessage());
                }
            }
        } else {
            reportError("Error no existe ningun archivo abierto.");
        }
        return null;
    }
}
