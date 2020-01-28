package com.neftxx.ast.expression;

import com.neftxx.ast.statement.method.MethodStm;
import com.neftxx.ast.statement.method.Parameter;
import com.neftxx.ast.util.Convert;
import com.neftxx.ast.util.RmbException;
import com.neftxx.scope.Binding;
import com.neftxx.scope.Scope;
import com.neftxx.type.RmbType;
import com.neftxx.type.TypeTool;
import com.neftxx.type.UndefinedType;
import com.neftxx.util.NodeInfo;

import java.util.ArrayList;

public class MethodCall extends Expression {
    public final String id;
    public final ArrayList<Expression> expressions;

    public MethodCall(NodeInfo info, String id, ArrayList<Expression> expressions) {
        super(info);
        this.id = id;
        this.expressions = expressions;
    }

    public MethodCall(NodeInfo info, String id) {
        this(info, id, new ArrayList<>());
    }

    @Override
    protected Object calculateValue(Scope scope) throws RmbException {
        var fileScope = scope.getGlobal();
        ArrayList<RmbType> rmbTypes = new ArrayList<>();
        ArrayList<Object> values = new ArrayList<>();
        for (var exp: expressions) {
            values.add(exp.interpret(scope));
            if (TypeTool.isUndefined(exp.type)) {
                reportError("Error en los parametros en la llamada a funcion " + id);
                this.type = UndefinedType.UNDEFINED;
                return null;
            }
            rmbTypes.add(exp.type);
        }

        MethodStm methodStm = fileScope.getMethod(id, rmbTypes);
        if (methodStm != null) {
            Scope methodScope = new Scope(null);
            int count = methodStm.parameters.size();
            String id;
            RmbType typeTemp;
            Object valueTemp;
            Parameter parameter;
            int i = 0;
            for (; i < count; i++) {
                parameter = methodStm.parameters.get(i);
                id = parameter.id;
                typeTemp = parameter.type;
                if (TypeTool.isChr(typeTemp)) {
                    valueTemp = Convert.toChar(rmbTypes.get(i), values.get(i));
                } else if (TypeTool.isDec(typeTemp)) {
                    valueTemp = Convert.toDouble(rmbTypes.get(i), values.get(i));
                } else if (TypeTool.isEnt(typeTemp)) {
                    valueTemp = Convert.toInt(rmbTypes.get(i), values.get(i));
                } else if (TypeTool.isBul(typeTemp)) {
                    valueTemp = Convert.toBoolean(rmbTypes.get(i), values.get(i));
                } else {
                    valueTemp = values.get(i);
                }
                Binding binding = new Binding(id, typeTemp, valueTemp, false);
                boolean ok = methodScope.addVariable(id, binding);
                if (!ok) {
                    reportError("Ha ocurrido un error en el parametro " + id + " al llamar la funcion " + methodStm.id);
                    return null;
                }
            }
            this.value = methodStm.interpret(methodScope);
            this.type = methodStm.type;
            return this.value;
        }
        this.type = UndefinedType.UNDEFINED;
        reportError("No existe el metodo con el nombre " + id + " con parametros " + rmbTypes.toString());
        return null;
    }
}
