package com.neftxx.ast.gui;

import com.neftxx.ast.AstNode;
import com.neftxx.ast.expression.Expression;
import com.neftxx.ast.expression.Identifier;
import com.neftxx.ast.expression.array.ArrayAccess;
import com.neftxx.ast.expression.array.ArrayNode;
import com.neftxx.ast.expression.array.RmbArray;
import com.neftxx.ast.expression.array.ValueNode;
import com.neftxx.ast.statement.field.FieldAccess;
import com.neftxx.ast.util.RmbException;
import com.neftxx.scope.Binding;
import com.neftxx.scope.Scope;
import com.neftxx.type.ArrayType;
import com.neftxx.type.TypeTool;
import com.neftxx.util.NodeInfo;

import java.util.ArrayList;

public class AccessToGuiMethodStat extends AstNode {
    public final FieldAccess fieldAccess;
    public final String id;
    public final ArrayList<Expression> parameters;

    public AccessToGuiMethodStat(NodeInfo info, FieldAccess fieldAccess, String id, ArrayList<Expression> parameters) {
        super(info);
        this.fieldAccess = fieldAccess;
        this.id = id;
        this.parameters = parameters;
    }

    @Override
    public Object interpret(Scope scope) {
        var currentScope = fieldAccess.interpret(scope);
        if (currentScope != null) {
            AstNode node = fieldAccess.getLast();
            if (node instanceof Identifier) {
                Identifier identifier = (Identifier) node;
                setValueId(identifier.id, scope, currentScope);
            } else if (node instanceof ArrayAccess) {
                try {
                    ArrayAccess arrayAccess = (ArrayAccess) node;
                    setValueArray(arrayAccess, scope, currentScope);
                } catch (Exception e) {
                    reportError(e.getMessage());
                }
            } else {
                reportError("Error no es un objeto para poder acceder a una variable.");
            }
        }
        return null;
    }

    private void setValueId(String idComponent, Scope mainScope, Scope scopeTemp) {
        Binding binding;
        if (mainScope == scopeTemp) {
            binding = mainScope.getVariable(idComponent);
        } else {
            binding = scopeTemp.getVariableLocal(idComponent);
        }

        if (binding != null) {
            if (TypeTool.isComponentType(binding.type)) {
                var val = binding.valueVar.value;
                if (val != null) {
                    try {
                        RmbComponent rmbComponent = (RmbComponent) val;
                        var methodCall = new MethodCallGui(info, rmbComponent, id, parameters);
                        methodCall.calculateValue(mainScope);
                    } catch (RmbException e) {
                        reportError(e.getMessage());
                    }
                } else {
                    reportError("Error al acceder al objeto ya que es null");
                }
            } else {
                reportError("No se puede acceder al metodo " + id + " del objeto " + binding.type);
            }
        } else {
            reportError("La variable " + id + " no ha sido declarada.");
        }
    }

    private void setValueArray(ArrayAccess arrayAccess, Scope mainScope, Scope scopeTemp) throws RmbException {
        Binding binding;
        if (mainScope == scopeTemp) {
            binding = mainScope.getVariable(arrayAccess.id);
        } else {
            binding = scopeTemp.getVariableLocal(arrayAccess.id);
        }

        if (binding != null) {
            if (TypeTool.isArray(binding.type)) {
                int numberDim = arrayAccess.getDimensions().size();
                ArrayType arrayType = (ArrayType) binding.type;
                if (arrayType.numberDim == numberDim) {
                    int[] indexes = new int[numberDim];
                    int i = 0;
                    for (; i < numberDim; i++) {
                        indexes[i] = arrayAccess.getDimensions().get(i).calculateDim(mainScope);
                    }
                    try {
                        Object val;
                        val = binding.valueVar.value;
                        ArrayNode arrayNode = null;
                        if (val instanceof RmbArray) {
                            RmbArray array = (RmbArray) val;
                            arrayNode = array.root;
                        } else {
                            arrayNode = (ArrayNode) val;
                        }
                        var node = arrayNode.getNode(RmbArray.POSITION_INITIAL, indexes);
                        if (node.isValueNode()) {
                            ValueNode valueNode = (ValueNode) node;
                            if (TypeTool.isComponentType(valueNode.type)) {
                                if (valueNode.valueVar.value != null) {
                                    RmbComponent rmbComponent = (RmbComponent) valueNode.valueVar.value;
                                    var methodCall = new MethodCallGui(info, rmbComponent, id, parameters);
                                    methodCall.calculateValue(mainScope);
                                } else {
                                    reportError("Error al acceder al objeto ya que es null");
                                }
                            } else {
                                reportError("No se puede acceder al metodo " + id + " del objeto " + valueNode.type);
                            }
                        } else {
                            reportError("No se puede acceder al componente en este arreglo.");
                        }
                    } catch (Exception e) {
                        reportError(e.getMessage());
                        reportError("Se ha accedido a una arreglo con un índice incorrecto. " +
                                "El índice es negativo o mayor o igual que el tamaño del arreglo.");
                    }
                } else {
                    reportError("No se puede acceder al componente en este arreglo.");
                }
            } else {
                reportError("La variable " + arrayAccess.id  + " no es un arreglo.");
            }
        } else {
            reportError("La variable " + arrayAccess.id + " no ha sido declarada.");
        }
    }
}
