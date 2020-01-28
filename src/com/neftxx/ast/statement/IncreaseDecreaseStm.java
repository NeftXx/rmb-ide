package com.neftxx.ast.statement;

import com.neftxx.ast.AstNode;
import com.neftxx.ast.expression.Identifier;
import com.neftxx.ast.expression.array.ArrayAccess;
import com.neftxx.ast.expression.array.ArrayNode;
import com.neftxx.ast.expression.array.RmbArray;
import com.neftxx.ast.expression.array.ValueNode;
import com.neftxx.ast.statement.field.FieldAccess;
import com.neftxx.ast.util.Convert;
import com.neftxx.ast.util.RmbException;
import com.neftxx.scope.Binding;
import com.neftxx.scope.Scope;
import com.neftxx.type.ArrayType;
import com.neftxx.type.TypeTool;
import com.neftxx.util.NodeInfo;

public class IncreaseDecreaseStm extends AstNode {
    public enum Operator {
        INCREASE,
        DECREASE
    }

    public final FieldAccess fieldAccess;
    public final Operator operator;

    public IncreaseDecreaseStm(NodeInfo info, FieldAccess fieldAccess, Operator operator) {
        super(info);
        this.fieldAccess = fieldAccess;
        this.operator = operator;
    }

    @Override
    public Object interpret(Scope scope) {
        var currentScope = fieldAccess.interpret(scope);
        if (currentScope != null) {
            AstNode node = fieldAccess.getLast();
            try {
                if (node instanceof Identifier) {
                    Identifier identifier = (Identifier) node;
                    setValueId(identifier.id, scope, currentScope);
                } else if (node instanceof ArrayAccess) {
                    ArrayAccess arrayAccess = (ArrayAccess) node;
                    setValueArray(arrayAccess, scope, currentScope);
                } else {
                    reportError("Error al intentar acceder a una variable.");
                }
            } catch (Exception e) {
                reportError(e.getMessage());
            }
        }
        return null;
    }

    private void setValueId(String id, Scope mainScope, Scope scopeTemp) throws RmbException {
        Binding binding;
        if (mainScope == scopeTemp) {
            binding = mainScope.getVariable(id);
        } else {
            binding = scopeTemp.getVariableLocal(id);
        }
        if (binding != null) {
            if (binding.isConstant) {
                reportError("El valor de una constante no puede cambiar");
                return;
            }
            Object value = binding.valueVar.value;
            int res;
            if (operator.equals(Operator.INCREASE)) res = 1;
            else res = -1;
            if (TypeTool.isEnt(binding.type)) {
                binding.valueVar.value = Convert.toInt(binding.type, value) + res;
            } else if (TypeTool.isDec(binding.type)) {
                binding.valueVar.value = Convert.toDouble(binding.type, value) + res;
            } else if (TypeTool.isChr(binding.type)) {
                binding.valueVar.value = Convert.toChar(binding.type, value) + res;
            } else {
                reportError("Error en este tipo de expresion no se puede usar el incremento o decremento");
            }
            return;
        }
        reportError("La variable " + id + " no ha sido declarada.");
    }

    private void setValueArray(ArrayAccess arrayAccess, Scope mainScope, Scope scopeTemp) throws RmbException {
        Binding binding;
        if (mainScope == scopeTemp) {
            binding = mainScope.getVariable(arrayAccess.id);
        } else {
            binding = scopeTemp.getVariableLocal(arrayAccess.id);
        }
        if (binding != null) {
            if (binding.isConstant) {
                reportError("El valor de una constante no puede cambiar");
                return;
            }
            if (TypeTool.isArray(binding.type)) {
                int numberDim = arrayAccess.getDimensions().size();
                ArrayType arrayType = (ArrayType) binding.type;
                if (arrayType.numberDim >= numberDim) {
                    int[] indexes = new int[numberDim];
                    int i = 0;
                    for (; i < numberDim; i++) {
                        indexes[i] = arrayAccess.getDimensions().get(i).calculateDim(mainScope);
                    }
                    try {
                        var temp = binding.valueVar.value;
                        ArrayNode array;
                        if (temp instanceof RmbArray) {
                            var rmbArray = (RmbArray) temp;
                            array = rmbArray.root;
                        } else {
                            array = (ArrayNode) temp;
                        }
                        var node = array.getNode(RmbArray.POSITION_INITIAL, indexes);
                        if (node.isValueNode()) {
                            ValueNode valueNode = (ValueNode) node;
                            Object value = valueNode.valueVar.value;
                            int res;
                            if (operator.equals(Operator.INCREASE)) res = 1;
                            else res = -1;
                            if (TypeTool.isEnt(valueNode.type)) {
                                valueNode.valueVar.value = Convert.toInt(valueNode.type, value) + res;
                            } else if (TypeTool.isDec(valueNode.type)) {
                                valueNode.valueVar.value = Convert.toDouble(valueNode.type, value) + res;
                            } else if (TypeTool.isChr(valueNode.type)) {
                                valueNode.valueVar.value = Convert.toChar(valueNode.type, value) + res;
                            } else {
                                reportError("Error en este tipo de expresion no se puede usar el incremento o decremento");
                            }
                            return;
                        } else {
                            reportError("Error en una variable de tipo " + node.type + " no se puede usar un incremento o decremento");
                            return;
                        }
                    } catch (Exception e) {
                        reportError("Se ha accedido a una arreglo con un índice incorrecto. El índice es negativo o mayor o igual que el tamaño del arreglo.");
                        return;
                    }
                } else {
                    reportError("Se ha accedido a una arreglo con un índice incorrecto. " +
                            "El índice es negativo o mayor o igual que el tamaño del arreglo.");
                    return;
                }
            }
            return;
        }
        reportError("La variable " + arrayAccess.id + " no ha sido declarada.");
    }
}
