package com.neftxx.ast.statement.method;

import com.neftxx.ast.AstNode;
import com.neftxx.ast.statement.BreakStm;
import com.neftxx.ast.statement.ContinueStm;
import com.neftxx.ast.statement.ReturnStm;
import com.neftxx.scope.FileScope;
import com.neftxx.type.TypeTool;
import com.neftxx.util.NodeInfo;
import com.neftxx.ast.statement.Block;
import com.neftxx.scope.Scope;
import com.neftxx.type.RmbType;

import java.util.ArrayList;

public class MethodStm extends AstNode {
    private FileScope myFileScope;
    public final RmbType type;
    public final String id;
    public final ArrayList<Parameter> parameters;
    public final Block block;

    public MethodStm(NodeInfo info, RmbType type, String id, ArrayList<Parameter> parameters, Block block) {
        super(info);
        this.type = type;
        this.id = id;
        this.parameters = parameters;
        this.block = block;
        myFileScope = new FileScope(info.filename);
    }

    public void setMyFileScope(FileScope fileScope) {
        this.myFileScope = fileScope;
    }

    public boolean isMain() {
        return TypeTool.isVoid(type) && id.equals("main") && parameters.isEmpty();
    }

    @Override
    public Object interpret(Scope scope) {
        scope.previous = myFileScope;
        scope.setGlobal(myFileScope);
        var valBlock = block.interpret(scope);
        if (valBlock instanceof BreakStm || valBlock instanceof ContinueStm) {
            reportError("No se encontro un bucle con cierre para la sentencia romper o siga.");
            return null;
        }

        if (valBlock instanceof ReturnStm) {
            ReturnStm returnStm = (ReturnStm) valBlock;
            Object value = returnStm.value;
            if (type.isAssignable(returnStm.type)) {
                return value;
            }
            reportError("El tipo de regresar " + returnStm.type + " no se puede convertir en el tipo " + type);
            return null;
        }
        if (!TypeTool.isVoid(type)) {
            reportError("No se encontro regresar para la funcion " + id);
        }
        return null;
    }
}
