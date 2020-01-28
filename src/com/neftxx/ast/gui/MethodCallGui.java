package com.neftxx.ast.gui;

import com.neftxx.ast.expression.Expression;
import com.neftxx.ast.expression.array.ArrayNode;
import com.neftxx.ast.expression.array.RmbArray;
import com.neftxx.ast.util.Convert;
import com.neftxx.ast.util.RmbException;
import com.neftxx.ast.util.StringAnalyzer;
import com.neftxx.scope.Scope;
import com.neftxx.type.*;
import com.neftxx.util.NodeInfo;

import java.util.ArrayList;

public class MethodCallGui extends Expression {
    protected static final String[] ATTRIBUTES = {
            "Settexto", "Setancho", "Setalto", "Setpos", "Gettexto", "Getancho", "Getalto", "Getpos"
    };

    public RmbComponent component;
    public String id;
    public ArrayList<Expression> expressions;

    public MethodCallGui(NodeInfo info, RmbComponent component, String id, ArrayList<Expression> expressions) {
        super(info);
        this.component = component;
        this.id = id;
        this.expressions = expressions;
    }

    @Override
    protected Object calculateValue(Scope scope) throws RmbException {
        if (id.equalsIgnoreCase(ATTRIBUTES[0])) {
            if (expressions.size() == 1) {
                var exp = expressions.get(0);
                var valExp = exp.interpret(scope);
                if (TypeTool.isArray(exp.type)) {
                    ArrayType arrayType = (ArrayType) exp.type;
                    if (TypeTool.isChr(arrayType.type) && arrayType.numberDim == 1) {
                        ArrayNode arrayNode;
                        if (valExp instanceof RmbArray) {
                            RmbArray rmbArray = (RmbArray) valExp;
                            arrayNode = rmbArray.root;
                        } else {
                            arrayNode = (ArrayNode) valExp;
                        }
                        String text = StringAnalyzer.obtenerCadena(arrayNode);
                        component.setText(text);
                        this.type = VoidType.ZRO;
                    } else {
                        reportError("Los tipos son incompatibles: " + arrayType + " no se puede convertir en RString.");
                        this.type = UndefinedType.UNDEFINED;
                    }
                    return null;
                } else if (TypeTool.isRString(exp.type)) {
                    String text = valExp.toString();
                    component.setText(text);
                    this.type = VoidType.ZRO;
                } else {
                    reportError("Los tipos son incompatibles: " + exp.type + " no se puede convertir en RString.");
                    this.type = UndefinedType.UNDEFINED;
                }
            } else {
                reportError("Error la cantidad de expresiones es diferente a la que se esperaba en la funcion Settexto.");
            }
        } else if (id.equalsIgnoreCase(ATTRIBUTES[1])) {
            if (expressions.size() == 1) {
                var exp = expressions.get(0);
                var valExp = exp.interpret(scope);
                int val = Convert.toInt(exp.type, valExp);
                component.setWidth(val);
                this.type = VoidType.ZRO;
                return null;
            } else {
                reportError("Error la cantidad de expresiones es diferente a la que se esperaba en la funcion Setancho.");
            }
        } else if (id.equalsIgnoreCase(ATTRIBUTES[2])) {
            if (expressions.size() == 1) {
                var exp = expressions.get(0);
                var valExp = exp.interpret(scope);
                int val = Convert.toInt(exp.type, valExp);
                component.setHeight(val);
                this.type = VoidType.ZRO;
                return null;
            } else {
                reportError("Error la cantidad de expresiones es diferente a la que se esperaba en la funcion Setalto.");
            }
        } else if (id.equalsIgnoreCase(ATTRIBUTES[3])) {
            if (expressions.size() == 2) {
                var exp1 = expressions.get(0);
                var exp2 = expressions.get(1);
                var valExp1 = exp1.interpret(scope);
                var valExp2 = exp2.interpret(scope);
                int x = Convert.toInt(exp1.type, valExp1);
                int y = Convert.toInt(exp2.type, valExp2);
                component.setPosition(x, y);
                this.type = VoidType.ZRO;
                return null;
            } else {
                reportError("Error la cantidad de expresiones es diferente a la que se esperaba en la funcion Setpos.");
            }
        } else if (id.equalsIgnoreCase(ATTRIBUTES[4])) {
            if (expressions.isEmpty()) {
                this.type = StringType.R_STRING;
                this.value = component.getText();
                return this.value;
            } else {
                reportError("Error la funcion Gettexto no espera ningún parametro.");
            }
        } else if (id.equalsIgnoreCase(ATTRIBUTES[5])) {
            if (expressions.isEmpty()) {
                this.type = PrimitiveType.ENT;
                this.value = component.getWidth();
                return this.value;
            } else {
                reportError("Error la funcion Getancho no espera ningún parametro.");
            }
        } else if (id.equalsIgnoreCase(ATTRIBUTES[6])) {
            if (expressions.isEmpty()) {
                this.type = PrimitiveType.ENT;
                this.value = component.getHeight();
                return this.value;
            } else {
                reportError("Error la funcion Getalto no espera ningún parametro.");
            }
        } else if (id.equalsIgnoreCase(ATTRIBUTES[7])) {
            if (expressions.isEmpty()) {
                RmbArray rmbArray = component.getPosition();
                this.type = rmbArray.type;
                this.value = rmbArray;
                return rmbArray;
            } else {
                reportError("Error la funcion Getpos no espera ningún parametro.");
            }
        } else {
            reportError("Error la funcion " + id + " no se encontro en este objeto.");
        }
        this.type = UndefinedType.UNDEFINED;
        return null;
    }
}
