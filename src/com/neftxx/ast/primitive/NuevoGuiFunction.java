package com.neftxx.ast.primitive;

import com.neftxx.ast.AstNode;
import com.neftxx.ast.gui.*;
import com.neftxx.scope.Scope;
import com.neftxx.type.RmbType;
import com.neftxx.type.TypeTool;
import com.neftxx.type.UndefinedType;
import com.neftxx.util.NodeInfo;

public class NuevoGuiFunction extends AstNode {
    public String id;
    public RmbComponent component;
    public RmbType type;

    public NuevoGuiFunction(NodeInfo info, String id) {
        super(info);
        this.id = id;
        component = null;
        type = UndefinedType.UNDEFINED;
    }

    @Override
    public Object interpret(Scope scope) {
        var binding = scope.getVariable(id);
        if (binding != null) {
            var type = binding.type;
            this.type = type;
            if (TypeTool.isButton(type)) {
                RmbButton rmbButton = new RmbButton();
                var fileScope = scope.getGlobal();
                var click = fileScope.getEvent(id);
                if (click != null) {
                    rmbButton.button.setOnAction(actionEvent -> {
                        Scope scopeLocal = new Scope(fileScope);
                        scopeLocal.setGlobal(fileScope);
                        click.interpret(scopeLocal);
                    });
                }
                component = rmbButton;
                binding.valueVar.value = component;
            } else if (TypeTool.isLabel(type)) {
                component = new RmbLabel();
                binding.valueVar.value = component;
            } else if (TypeTool.isNumericField(type)) {
                component = new RmbNumericField();
                binding.valueVar.value = component;
            } else if (TypeTool.isPasswordField(type)) {
                component = new RmbPasswordField();
                binding.valueVar.value = component;
            } else if (TypeTool.isTextArea(type)) {
                component = new RmbTextArea();
                binding.valueVar.value = component;
            } else if (TypeTool.isTextField(type)) {
                component = new RmbTextField();
                binding.valueVar.value = component;
            } else {
                this.type = UndefinedType.UNDEFINED;
                reportError("La variable " + id + " no es un componente GUI.");
            }
        } else {
            this.type = UndefinedType.UNDEFINED;
            reportError("La variable " + id + " no ha sido declarada.");
        }
        return null;
    }
}
