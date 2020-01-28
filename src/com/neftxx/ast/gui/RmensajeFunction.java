package com.neftxx.ast.gui;

import com.neftxx.ast.AstNode;
import com.neftxx.ast.expression.Expression;
import com.neftxx.ast.util.StringAnalyzer;
import com.neftxx.scope.Scope;
import com.neftxx.util.DialogUtils;
import com.neftxx.util.NodeInfo;
import javafx.scene.control.Alert;

import java.util.ArrayList;

public class RmensajeFunction extends AstNode {
    public final ArrayList<Expression> expressions;

    public RmensajeFunction(NodeInfo info, ArrayList<Expression> expressions) {
        super(info);
        this.expressions = expressions;
    }

    @Override
    public Object interpret(Scope scope) {
        String val = StringAnalyzer.getCad(this, expressions, scope);
        if (val != null) {
            Alert infoDialog = new Alert(Alert.AlertType.INFORMATION);
            infoDialog.setTitle("Rmensaje");
            infoDialog.setHeaderText(val);
            infoDialog.setContentText("");
            infoDialog.showAndWait();
        }
        return null;
    }
}
