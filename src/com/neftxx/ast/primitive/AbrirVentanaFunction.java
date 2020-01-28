package com.neftxx.ast.primitive;

import com.neftxx.ast.AstNode;
import com.neftxx.scope.Scope;
import com.neftxx.util.NodeInfo;

public class AbrirVentanaFunction extends AstNode {
    public String frame;

    public AbrirVentanaFunction(NodeInfo info, String frame) {
        super(info);
        this.frame = frame;
    }

    @Override
    public Object interpret(Scope scope) {
        var fileScope = scope.getGlobal();
        var guiScope = fileScope.getFrame(frame);
        if (guiScope != null) {
            var gui = guiScope.createFrame();
            gui.showAndWait();
        } else {
            reportError("No se encontro un formulario con el nombre " + frame);
        }
        return null;
    }
}
