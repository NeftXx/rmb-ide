package com.neftxx.ast.statement.field;

import com.neftxx.ast.AstNode;
import com.neftxx.ast.expression.Identifier;
import com.neftxx.ast.expression.array.*;
import com.neftxx.ast.util.RmbException;
import com.neftxx.ast.expression.Expression;
import com.neftxx.ast.util.StringAnalyzer;
import com.neftxx.scope.Binding;
import com.neftxx.scope.Scope;
import com.neftxx.scope.ValueVar;
import com.neftxx.type.*;
import com.neftxx.ast.util.Convert;
import com.neftxx.util.NodeInfo;

public class VarAssignment extends AstNode {
    public final FieldAccess fieldAccess;
    public final Expression expression;

    public VarAssignment(NodeInfo info, FieldAccess fieldAccess, Expression expression) {
        super(info);
        this.fieldAccess = fieldAccess;
        this.expression = expression;
    }

    @Override
    public Object interpret(Scope scope) {
        var currentScope = fieldAccess.interpret(scope);
        if (currentScope != null) {
            AstNode node = fieldAccess.getLast();
            if (node instanceof Identifier) {
                Identifier identifier = (Identifier) node;
                return setValueId(identifier.id, scope, currentScope);
            } else if (node instanceof ArrayAccess) {
                try {
                    ArrayAccess arrayAccess = (ArrayAccess) node;
                    return setValueArray(arrayAccess, scope, currentScope);
                } catch (Exception e) {
                    reportError(e.getMessage());
                }
            } else {
                reportError("Error al intentar acceder a una variable.");
            }
        }
        return null;
    }

    private Object setValueArray(ArrayAccess arrayAccess, Scope mainScope, Scope scopeTemp) throws RmbException {
        Binding binding;
        if (mainScope == scopeTemp) {
            binding = mainScope.getVariable(arrayAccess.id);
        } else {
            binding = scopeTemp.getVariableLocal(arrayAccess.id);
        }
        if (binding != null) {
            if (binding.isConstant) {
                reportError("El valor de una constante no puede cambiar");
                return null;
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
                        Object value = expression.interpret(mainScope);
                        var expType = expression.type;

                        if (TypeTool.isUndefined(expType)) {
                            reportError("Error al calcular la expresion al momento de asignar a la variable "
                                    + arrayAccess.id);
                            return null;
                        } else {
                            Object val = binding.valueVar.value;
                            ArrayNode arrayNode;
                            if (val instanceof RmbArray) {
                                RmbArray array = (RmbArray) val;
                                arrayNode = array.root;
                            } else {
                                arrayNode = (ArrayNode) val;
                            }
                            var node = arrayNode.getNode(RmbArray.POSITION_INITIAL, indexes);
                            var typeArrayType = arrayType.type;
                            if (node.isValueNode()) {
                                ValueNode valueNode = (ValueNode) node;

                                if (TypeTool.isReference(expType)) {
                                    Binding ref = (Binding) value;
                                    valueNode.valueVar = ref.valueVar;
                                    return null;
                                }

                                if (createFusion(mainScope, value, expType, typeArrayType, valueNode.valueVar))
                                    return null;

                                return getObject(value, expType, typeArrayType, valueNode.valueVar);
                            } else {
                                if (typeArrayType.isAssignable(expType)) {
                                    ArrayNode newNode = (ArrayNode) value;
                                    arrayNode.setNode(RmbArray.POSITION_INITIAL, indexes, newNode);
                                } else {
                                    reportError("Los tipos son incompatibles: " + expType + " no se puede convertir en " + typeArrayType);
                                }
                            }
                        }

                    } catch (Exception e) {
                        reportError(e.getMessage());
                        reportError("Se ha accedido a una arreglo con un índice incorrecto. " +
                                "El índice es negativo o mayor o igual que el tamaño del arreglo.");
                        return null;
                    }
                } else {
                    reportError("Se ha accedido a una arreglo con un índice incorrecto. " +
                            "El índice es negativo o mayor o igual que el tamaño del arreglo.");
                    return null;
                }
            } else {
                reportError("La variable " + arrayAccess.id  + " no es un arreglo.");
                return null;
            }
        }
        reportError("La variable " + arrayAccess.id + " no ha sido declarada.");
        return null;
    }

    private Object setValueId(String id, Scope mainScope, Scope scopeTemp) {
        Binding binding;
        if (mainScope == scopeTemp) {
            binding = mainScope.getVariable(id);
        } else {
            binding = scopeTemp.getVariableLocal(id);
        }

        if (binding != null) {
            if (binding.isConstant) {
                reportError("El valor de una constante no puede cambiar");
                return null;
            }

            if (expression instanceof ArrayInitializer) {
                ArrayInitializer initializer = (ArrayInitializer) expression;
                var array = initializer.createArray(binding.type, mainScope);
                try {
                    return getObject(array, array.type, binding.type, binding.valueVar);
                } catch (Exception e) {
                    reportError(e.getMessage());
                    return null;
                }
            }
            else {
                Object value = expression.interpret(mainScope);
                var expType = expression.type;
                if (TypeTool.isRString(binding.type) && binding.type.isAssignable(expType)) {
                    if (TypeTool.isArray(expType)) {
                        if (value instanceof RmbArray) {
                            value = StringAnalyzer.obtenerCadena(((RmbArray) value).root);
                        } else if (value instanceof ArrayNode) {
                            value = StringAnalyzer.obtenerCadena((ArrayNode) value);
                        }
                    }
                    binding.valueVar.value = value.toString();
                    return null;
                }

                if (TypeTool.isReference(expType)) {
                    Binding ref = (Binding) value;
                    if (ref.type.isAssignable(expType)) {
                        scopeTemp.changeReference(id, ref);
                    } else {
                        reportError("Los tipos son incompatibles: " + ref.type + " no se puede convertir en " + expType);
                    }
                    return null;
                }

                if (createFusion(mainScope, value, expType, binding.type, binding.valueVar)) return null;

                try {
                    return getObject(value, expType, binding.type, binding.valueVar);
                } catch (Exception e) {
                    reportError(e.getMessage());
                    return null;
                }
            }
        }
        reportError("La variable " + id + " no ha sido declarada.");
        return null;
    }

    private boolean createFusion(Scope mainScope, Object value, RmbType expType, RmbType typeTemp, ValueVar valueVar) {
        if (TypeTool.isReserva(expType) && TypeTool.isFusion(typeTemp)) {
            FusionType fusionType = (FusionType) typeTemp;
            var fileScope = mainScope.getGlobal();
            var fusion = fileScope.getFusion(fusionType.getName());
            if (fusion != null) {
                int res = (int) value;
                if (res >= fusion.getSize()) {
                    value = fusion.interpret(mainScope);
                    valueVar.value = value;
                } else {
                    reportError("El tamaño de memoria es insuficiente para crear el fusion " + fusion.id);
                }
                return true;
            }
            reportError("No se encontro algun fusion con nombre " + fusionType.getName() + " en este archivo.");
            return true;
        }
        return false;
    }

    private Object getObject(Object value, RmbType expType, RmbType mainType, ValueVar valueVar) throws RmbException {
        if (mainType.isAssignable(expType)) {
            if (TypeTool.isEnt(mainType))
                value = Convert.toInt(expType, value);
            else if (TypeTool.isDec(mainType))
                value = Convert.toDouble(expType, value);
            else if (TypeTool.isBul(mainType))
                value = Convert.toBoolean(expType, value);
            else if (TypeTool.isChr(mainType))
                value = Convert.toChar(expType, value);
            else if (TypeTool.isArray(mainType)) {
                if (value instanceof ArrayNode) {
                    value = new RmbArray((ArrayType) expType, (ArrayNode) value);
                }
            } else if (TypeTool.isRString(mainType)) {
                if (TypeTool.isArray(expType)) {
                    if (value instanceof RmbArray) {
                        value = StringAnalyzer.obtenerCadena(((RmbArray) value).root);
                    } else if (value instanceof ArrayNode) {
                        value = StringAnalyzer.obtenerCadena((ArrayNode) value);
                    }
                } else {
                    value = value.toString();
                }
            }
            valueVar.value = value;
            return null;
        }
        reportError("Los tipos son incompatibles: " + expression.type + " no se puede convertir en " + mainType);
        return null;
    }
}
