package com.neftxx.ast.statement.field;

import com.neftxx.ast.expression.Expression;
import com.neftxx.ast.expression.array.*;
import com.neftxx.ast.util.StringAnalyzer;
import com.neftxx.scope.Binding;
import com.neftxx.type.*;
import com.neftxx.util.NodeInfo;
import com.neftxx.scope.Scope;

public class FieldDeclaration extends VarDeclaration {
    protected Expression expression;

    public FieldDeclaration(NodeInfo info, RmbType type, String id) {
        super(info, type, id);
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Object interpret(Scope scope) {
        // busco el simbolo en el entorno actual
        Binding binding = scope.getVariable(id);
        // si es nulo significa que existe, por lo tanto seria un error
        if (binding == null) {
            RmbType newType = this.type;
            // valor para el nuevo simbolo
            Object value = null;
            // numero de dimensiones
            int numberDim = getNumberDimensions();
            // si la expresion es nula, hay que crear valores por defecto
            if (expression == null) {
                // si el numero de dimensiones es cero, significa que no es un arreglo
                if (numberDim == 0) {
                    if (TypeTool.isBul(type)) value = false;
                    else if (TypeTool.isEnt(type)) value = 0;
                    else if (TypeTool.isDec(type)) value = 0.0;
                    else if (TypeTool.isChr(type)) value = '\0';
                    else if (TypeTool.isRString(type)) value = "";
                }
                // en caso contrario se crea un arreglo nuevo
                else {
                    try {
                        int[] indexes = new int[numberDim];
                        int i = 0;
                        for (; i < numberDim; i ++) {
                            indexes[i] = dimensions.get(i).calculateDim(scope);
                            if (indexes[i] < 0) {
                                reportError("En este tipo de declaración no es posible crear arreglos sin indicar el tamaño de dimensiones o que sea menor a 0.");
                                return null;
                            }
                        }
                        var temp = ArrayFactory.createArray(numberDim, indexes, type);
                        value = temp;
                        newType = temp.type;
                    } catch (Exception e) {
                        reportError("No se pudo crear el id. " + e.getMessage());
                        return null;
                    }
                }
            }
            // Calcular la expresion
            else {
                // si la expresion no es nula verifico el tipo de expresion
                // si la expresion es de tipo de inicializacion de arreglo es decir:
                // { {...} , {...}, ... }
                if (expression instanceof ArrayInitializer) {
                    // creo el arreglo
                    ArrayInitializer initializer = (ArrayInitializer) expression;
                    var array = initializer.createArray(type, scope);

                    int i;
                    StringBuilder builder = new StringBuilder();
                    // si el numero de dimensiones de la variable es menor o igual a cero
                    // no es un arreglo asi que deberia mostrar error
                    if (numberDim <= 0) {
                        builder.append("Los tipos son incompatibles: ").append(initializer.type)
                                .append(" no se puede convertir en ").append(type);
                        reportError(builder.toString());
                        return null;
                    }

                    // ya que se que tanto la variable como la expresion son arreglos
                    // verifico que el numero de dimensiones sea el mismo
                    boolean ok = array.verifyNumberDimensions(numberDim, builder);
                    // si es el mismo paso a verificar otras cosas
                    if (ok) {
                        try {
                            // creo un arreglo de para guardar los indices
                            int[] indexes = new int[numberDim];
                            ok = true;
                            // calculo cada una de las dimensiones
                            // verifico si todas los indices son -1
                            // si lo son el arreglo se puede asignar
                            // esto lo hice asi para poder asignar arreglos del tipo:
                            // ent a[] = {1, 2 , 3}; donde no es necesario especificar el indice
                            for (i = 0; i < numberDim; i++) {
                                indexes[i] = dimensions.get(i).calculateDim(scope);
                                ok = ok && indexes[i] <= -1;
                            }
                            if (ok) {
                                // por lo tanto solamente lo asigno
                                value = array;
                                newType = array.type;
                            } else {
                                // si no mando a verificar los tamaños
                                ok = array.verifySizeOfDimensions(indexes, builder);
                                if (ok) {
                                    value = array;
                                    newType = array.type;
                                } else {
                                    String[] errors = builder.toString().split("\n");
                                    for(String error: errors) {
                                        reportError(error);
                                    }
                                    return null;
                                }
                            }
                        } catch (Exception e) {
                            reportError("No se pudo crear el id. " + e.getMessage());
                            return null;
                        }
                    } else {
                        String[] errors = builder.toString().split("\n");
                        for(String error: errors) {
                            reportError(error);
                        }
                        return null;
                    }
                }
                else {
                    RmbType expType;
                    if (TypeTool.isRString(type) && expression instanceof CharacterArray) {
                        var characterArray = (CharacterArray) expression;
                        value = characterArray.text;
                        expType = StringType.R_STRING;
                    } else {
                        value = expression.interpret(scope);
                        expType = expression.type;
                    }

                    if (TypeTool.isUndefined(expType)) {
                        reportError("Los tipos son incompatibles: " + expType + " no se puede convertir en " + type);
                        return null;
                    }

                    if (numberDim > 0) {
                        newType = new ArrayType(numberDim, type);
                    }

                    if (TypeTool.isReference(expType)) {
                        if (value instanceof Binding) {
                            Binding ref = (Binding) value;
                            if (ref.type.isAssignable(type)) {
                                scope.addVariable(id, ref);
                            } else {
                                reportError("Los tipos son incompatibles: " + ref.type + " no se puede convertir en " + type);
                            }
                        }
                        return null;
                    }

                    if (!type.isAssignable(expType)) {
                        reportError("Los tipos son incompatibles: " + expType + " no se puede convertir en " + type);
                        return null;
                    }

                    if (TypeTool.isFusion(type) && TypeTool.isReserva(expType)) {
                        FusionType fusionType = (FusionType) type;
                        var fileScope = scope.getGlobal();
                        var fusion = fileScope.getFusion(fusionType.getName());
                        if (fusion != null) {
                            int res = (int) value;
                            if (res >= fusion.getSize()) {
                                value = fusion.interpret(scope);
                                scope.addVariable(id, new Binding(id, type, value, false));
                            } else {
                                reportError("El tamaño de memoria es insuficiente para crear el fusion " + fusion.id);
                            }
                            return null;
                        }
                        reportError("No se encontro algun fusion con nombre " + fusionType.getName() + " en este archivo.");
                        return null;
                    }
                }
            }
            if (TypeTool.isArray(newType)) {
                if (value instanceof ArrayNode) {
                    value = new RmbArray((ArrayType) newType, (ArrayNode) value);
                }
            } else if (TypeTool.isRString(newType)) {
                if (value instanceof RmbArray) {
                    value = StringAnalyzer.obtenerCadena(((RmbArray) value).root);
                } else if (value instanceof ArrayNode) {
                    value = StringAnalyzer.obtenerCadena((ArrayNode) value);
                }
            }
            scope.addVariable(id, new Binding(id, newType, value, false));
        } else {
            reportError("Error la variable " + id + " ya ha sido declarada en este entorno.");
        }
        return null;
    }
}
