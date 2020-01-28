package com.neftxx.ast.expression;

import com.neftxx.ast.expression.array.ArrayAccess;
import com.neftxx.ast.expression.array.ArrayNode;
import com.neftxx.ast.expression.array.RmbArray;
import com.neftxx.ast.expression.array.ValueNode;
import com.neftxx.ast.util.Convert;
import com.neftxx.ast.util.RmbException;
import com.neftxx.constant.Rmb;
import com.neftxx.scope.Binding;
import com.neftxx.scope.Scope;
import com.neftxx.type.ArrayType;
import com.neftxx.type.TypeTool;
import com.neftxx.type.UndefinedType;
import com.neftxx.util.NodeInfo;

public class IncreaseDecrease extends Expression {
    public enum Operator {
        INCREASE,
        DECREASE
    }

    public final Operator operator;
    public final Expression expression;

    public IncreaseDecrease(NodeInfo info, Expression expression, Operator operator) {
        super(info);
        this.expression = expression;
        this.operator = operator;
    }

    @Override
    protected Object calculateValue(Scope scope) throws RmbException {
        int res;
        if (operator.equals(Operator.INCREASE)) res = 1;
        else res = -1;

        if (expression instanceof Identifier) {
            Identifier identifier = (Identifier) expression;
            var binding = scope.getVariable(identifier.id);
            if (binding != null) {
                Object value = binding.valueVar.value;
                if (binding.isConstant) {
                    reportError("El valor de una constante no puede cambiar");
                    this.type = UndefinedType.UNDEFINED;
                    return null;
                }
                if (TypeTool.isEnt(binding.type)) {
                    this.type = binding.type;
                    binding.valueVar.value = Convert.toInt(binding.type, value) + res;
                } else if (TypeTool.isDec(binding.type)) {
                    this.type = binding.type;
                    binding.valueVar.value = Convert.toDouble(binding.type, value) + res;
                } else if (TypeTool.isChr(binding.type)) {
                    this.type = binding.type;
                    binding.valueVar.value = Convert.toChar(binding.type, value) + res;
                } else {
                    reportError("Error en este tipo de expresion no se puede usar el incremento o decremento");
                    return null;
                }
                value = binding.valueVar.value;
                return value;
            }
            identifier.reportError("No se ha declarado la variable " + identifier.id + " en este entorno.");
        } else if (expression instanceof VarAccess) {
            VarAccess varAccess = (VarAccess) expression;
            var val = varAccess.expression.interpret(scope);
            if (TypeTool.isFusion(varAccess.expression.type)) {
                if (val != null) {
                    Scope scopeObj = (Scope) val;

                    if (varAccess.identifier instanceof Identifier) {
                        var identifier = (Identifier) varAccess.identifier;
                        String id = identifier.id;
                        Binding binding;
                        binding = scopeObj.getVariableLocal(id);
                        if (binding != null) {
                            Object value;
                            value = binding.valueVar.value;
                            this.type = binding.type;
                            if (TypeTool.isEnt(binding.type)) {
                                binding.valueVar.value = Convert.toInt(binding.type, value) + res;
                            } else if (TypeTool.isDec(binding.type)) {
                                binding.valueVar.value = Convert.toDouble(binding.type, value) + res;
                            } else if (TypeTool.isChr(binding.type)) {
                                binding.valueVar.value = Convert.toChar(binding.type, value) + res;
                            } else {
                                this.type = UndefinedType.UNDEFINED;
                                reportError("Error en este tipo de expresion no se puede usar el incremento o decremento");
                                return null;
                            }
                            value = binding.valueVar.value;
                            return value;
                        }
                        reportError("La variable " + id + " no existe en el objeto " + expression.type);
                    } else if (varAccess.identifier instanceof ArrayAccess) {
                        ArrayAccess arrayAccess = (ArrayAccess) varAccess.identifier;
                        String id = arrayAccess.id;
                        var binding = scopeObj.getVariableLocal(id);
                        if (binding != null) {
                            this.value = binding.valueVar.value;
                            if (TypeTool.isEnt(binding.type)) {
                                this.type = arrayAccess.type;
                                binding.valueVar.value = Convert.toInt(binding.type, value) + res;
                            } else if (TypeTool.isDec(binding.type)) {
                                this.type = arrayAccess.type;
                                binding.valueVar.value = Convert.toDouble(binding.type, value) + res;
                            } else if (TypeTool.isChr(binding.type)) {
                                this.type = arrayAccess.type;
                                binding.valueVar.value = Convert.toChar(binding.type, value) + res;
                            } else {
                                this.type = UndefinedType.UNDEFINED;
                                reportError("Error en este tipo de expresion no se puede usar el incremento o decremento");
                                return null;
                            }
                            return this.value;
                        }
                        reportError("La variable " + id + " no existe en el objeto " + expression.type);
                    }
                }
            }
        } else if (expression instanceof ArrayAccess) {
            ArrayAccess arrayAccess = (ArrayAccess) expression;
            var binding = scope.getVariable(arrayAccess.id);
            if (binding != null) {
                if (TypeTool.isArray(binding.type)) {
                    int numberDim = arrayAccess.getDimensions().size();
                    ArrayType arrayType = (ArrayType) binding.type;
                    if (numberDim <= arrayType.numberDim) {
                        int[] indexes = new int[numberDim];
                        int i = 0;
                        for (; i < numberDim; i++) {
                            indexes[i] = arrayAccess.getDimensions().get(i).calculateDim(scope);
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
                                if (TypeTool.isEnt(valueNode.type)) {
                                    this.type = valueNode.type;
                                    valueNode.valueVar.value = Convert.toInt(valueNode.type, value) + res;
                                } else if (TypeTool.isDec(valueNode.type)) {
                                    this.type = valueNode.type;
                                    valueNode.valueVar.value = Convert.toDouble(valueNode.type, value) + res;
                                } else if (TypeTool.isChr(valueNode.type)) {
                                    this.type = valueNode.type;
                                    valueNode.valueVar.value = Convert.toChar(valueNode.type, value) + res;
                                } else {
                                    reportError("Error en este tipo de expresion no se puede usar el incremento o decremento");
                                    return null;
                                }
                                this.value = valueNode.valueVar.value;
                                return this.value;
                            } else {
                                this.type = UndefinedType.UNDEFINED;
                                reportError("Error en una variable de tipo " + node.type + " no se puede usar un incremento o decremento");
                                return null;
                            }
                        } catch (IndexOutOfBoundsException e) {
                            reportError("Se ha accedido a una arreglo con un índice incorrecto. El índice es negativo o mayor o igual que el tamaño del arreglo.");
                            return null;
                        }
                    } else {
                        reportError("Se ha accedido a una arreglo con un índice incorrecto. El índice es negativo o mayor o igual que el tamaño del arreglo.");
                        return null;
                    }
                }
                reportError("No se puede acceder a una posicion ya que la variable " + arrayAccess.id + " no es un arreglo.");
                return null;
            }
            reportError("No se ha declarado la variable " + arrayAccess.id + " en este entorno.");
        } else {
            var val = expression.interpret(scope);
            if (TypeTool.isEnt(expression.type)) {
                this.type = expression.type;
                return Convert.toInt(expression.type, val) + res;
            } else if (TypeTool.isDec(expression.type)) {
                this.type = expression.type;
                return Convert.toDouble(expression.type, val) + res;
            } else if (TypeTool.isChr(expression.type)) {
                this.type = expression.type;
                return Convert.toChar(expression.type, val) + res;
            } else {
                reportError("Error en este tipo de expresion no se puede usar el incremento o decremento");
            }
        }
        return null;
    }
}
