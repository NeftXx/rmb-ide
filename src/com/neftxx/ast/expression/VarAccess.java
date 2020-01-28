package com.neftxx.ast.expression;

import com.neftxx.ast.expression.array.ArrayAccess;
import com.neftxx.ast.util.RmbException;
import com.neftxx.scope.Scope;
import com.neftxx.type.TypeTool;
import com.neftxx.util.NodeInfo;

public class VarAccess extends Expression {
    public final Expression expression;
    public final Expression identifier;

    public VarAccess(NodeInfo info, Expression expression, Expression identifier) {
        super(info);
        this.expression = expression;
        this.identifier = identifier;
    }

    public String getId() {
        if (identifier instanceof Identifier) {
            return ((Identifier) identifier).id;
        } else if (identifier instanceof ArrayAccess) {
            return ((ArrayAccess) identifier).id;
        }
        return null;
    }

    @Override
    protected Object calculateValue(Scope scope) throws RmbException {
        // analizar la expresion
        var val = expression.interpret(scope);
        // Si la expresion es un objeto se puede acceder a la variable
        if (TypeTool.isFusion(expression.type)) {
            // Si es un objeto entonces el valor se puede convertir en un entorno
            if (val != null) {
                Scope obj = (Scope) val;
                // si el identificador es en realidad una instancia de identificador
                if (identifier instanceof Identifier) {
                    // Obtengo el id
                    String id = ((Identifier) identifier).id;
                    // busco el simbolo en las variables locales del entorno del objeto
                    var res = obj.getVariableLocal(id);
                    // obtengo el tipo y el valor del simbolo
                    if (res != null) {
                        type = res.type;
                        value = res.valueVar.value;
                        return value;
                    }
                    reportError("La variable " + id + " no existe en el objeto " + expression.type);
                }
                // Si el identificador un acceso a un arreglo
                else if (identifier instanceof ArrayAccess) {
                    ArrayAccess arrayAccess = (ArrayAccess) identifier;
                    // obtengo el id
                    String id = arrayAccess.id;
                    // busco el simbolo en las variables locales del entorno del objeto
                    var res = obj.getVariableLocal(id);
                    if (res != null) {
                        value = arrayAccess.getValue(scope, res);
                        type = arrayAccess.type;
                        return  value;
                    }
                    reportError("La variable " + id + " no existe en el objeto " + expression.type);
                } else {
                    reportError("Error en la expresion");
                    return null;
                }
            } else {
                reportError("Error al acceder al objeto ya que es null");
            }
            return null;
        }
        reportError("No se puede acceder a la variable del objeto " + expression.type);
        return null;
    }
}
