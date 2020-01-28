package com.neftxx.ast.primitive;

import com.neftxx.ast.AstNode;
import com.neftxx.ast.statement.Block;
import com.neftxx.scope.Scope;
import com.neftxx.util.NodeInfo;

public class GuiFunction extends AstNode {
    public String id1;
    public String id2;
    public Block block;

    public GuiFunction(NodeInfo info, String id1, String id2, Block block) {
        super(info);
        this.id1 = id1;
        this.id2 = id2;
        this.block = block;
    }

    @Override
    public Object interpret(Scope scope) {
        if (id1.equals("R") && id2.equals("iniciar_ventana")) {
            return new IniciarVentanaFunction(info, block);
        } else if (id2.equals("al_dar_click")) {
            return new AlDarClickFunction(info, block);
        } else {
            reportError("No se esperaba " + id1 + " y el id " + id2 + " no es una funcion de GUI.");
        }
        return null;
    }
}
