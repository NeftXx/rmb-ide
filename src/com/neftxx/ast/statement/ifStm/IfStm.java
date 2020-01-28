package com.neftxx.ast.statement.ifStm;

import com.neftxx.ast.AstNode;
import com.neftxx.scope.Scope;
import com.neftxx.util.NodeInfo;

import java.util.ArrayList;

public class IfStm extends AstNode {
    public final ArrayList<SubIfStm> subIfStms;

    public IfStm(NodeInfo info, SubIfStm subIfStm) {
        super(info);
        subIfStms = new ArrayList<>();
        subIfStms.add(subIfStm);
    }

    public IfStm(NodeInfo info, SubIfStm subIfStm, ArrayList<SubIfStm> subIfStms) {
        this(info, subIfStm);
        this.subIfStms.addAll(subIfStms);
    }

    public IfStm(NodeInfo info, SubIfStm subIfStm, ArrayList<SubIfStm> subIfStms, SubIfStm subElse) {
        this(info, subIfStm, subIfStms);
        this.subIfStms.add(subElse);
    }

    public IfStm(NodeInfo info, SubIfStm subIfStm, SubIfStm subElse) {
        super(info);
        this.subIfStms = new ArrayList<>();
        this.subIfStms.add(subIfStm);
        this.subIfStms.add(subElse);
    }

    @Override
    public Object interpret(Scope scope) {
        Object val;
        for(var subIf: subIfStms) {
            val = subIf.interpret(scope);
            if(subIf.getCondValue()) {
                return val;
            }
        }
        return null;
    }
}
