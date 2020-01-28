package com.neftxx.ast.primitive;

import com.neftxx.ast.AstNode;
import com.neftxx.ast.gui.*;
import com.neftxx.ast.statement.Block;
import com.neftxx.scope.Scope;
import com.neftxx.type.TypeTool;
import com.neftxx.util.NodeInfo;

public class IniciarVentanaFunction extends AstNode {
    public Block block;

    public IniciarVentanaFunction(NodeInfo info, Block block) {
        super(info);
        this.block = block;
    }

    @Override
    public RmbFrame interpret(Scope scope) {
        RmbFrame frame = new RmbFrame();
        if (block.astNodes != null) {
            for (var node: block.astNodes) {
                node.interpret(scope);
                if (node instanceof NuevoGuiFunction) {
                    var newGuiFunction = (NuevoGuiFunction) node;
                    if (newGuiFunction.component != null) {
                        var type = newGuiFunction.type;
                        if (TypeTool.isButton(type)) {
                            frame.addElement(((RmbButton) newGuiFunction.component).button);
                        } else if (TypeTool.isLabel(type)) {
                            frame.addElement(((RmbLabel) newGuiFunction.component).label);
                        } else if (TypeTool.isNumericField(type)) {
                            frame.addElement(((RmbNumericField) newGuiFunction.component).spinner);
                        } else if (TypeTool.isPasswordField(type)) {
                            frame.addElement(((RmbPasswordField) newGuiFunction.component).passwordField);
                        } else if (TypeTool.isTextArea(type)) {
                            frame.addElement(((RmbTextArea) newGuiFunction.component).textArea);
                        } else if (TypeTool.isTextField(type)) {
                            frame.addElement(((RmbTextField) newGuiFunction.component).textField);
                        }
                    }
                } else if (node instanceof AltoAnchoFunction) {
                    var function = (AltoAnchoFunction) node;
                    if (function.width > -1 && function.height > -1) {
                        frame.setSize(function.width, function.height);
                    }
                }
            }
        }
        return frame;
    }
}
