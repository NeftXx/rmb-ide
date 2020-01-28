package com.neftxx.ast.expression.array;

import com.neftxx.ast.expression.Expression;
import com.neftxx.ast.statement.field.Dimension;
import com.neftxx.ast.util.RmbException;
import com.neftxx.scope.Binding;
import com.neftxx.scope.Scope;
import com.neftxx.type.ArrayType;
import com.neftxx.type.TypeTool;
import com.neftxx.util.NodeInfo;

import java.util.ArrayList;

public class ArrayAccess extends Expression {
    public final String id;
    private ArrayList<Dimension> dimensions;

    public ArrayAccess(NodeInfo info, String id) {
        super(info);
        this.id = id;
        this.dimensions = new ArrayList<>();
    }

    public void addDimension(Dimension dimension) {
        dimensions.add(dimension);
    }

    public ArrayList<Dimension> getDimensions() {
        return dimensions;
    }

    @Override
    protected Object calculateValue(Scope scope) throws RmbException {
        var binding = scope.getVariable(id);
        return getValue(scope, binding);
    }

    public Object getValue(Scope scope, Binding binding) throws RmbException {
        // Si el simbolo es vacio reportar error, ya que no existe
        if (binding != null) {
            // Si el simbolo es un array se puede acceder a una posicion
            if (TypeTool.isArray(binding.type)) {
                // obtengo el numero de dimensiones
                int numberDim = dimensions.size();
                ArrayType arrayType = (ArrayType) binding.type;
                // si el numero de dimensiones es igual o menor al numero que se quieren acceder
                // por lo tanto las posiciones pueden que existan.
                if (numberDim <= arrayType.numberDim) {
                    // arreglo para guardar los indices de las posiciones que quiero acceder
                    int[] indexes = new int[numberDim];
                    int i = 0;
                    for (; i < numberDim; i ++) {
                        indexes[i] = dimensions.get(i).calculateDim(scope);
                    }
                    // un try catch por si se excede a la posicion que se quiere acceder
                    try {
                        // Obtengo el arreglo
                        var temp = binding.valueVar.value;
                        ArrayNode array;
                        if (temp instanceof RmbArray) {
                            RmbArray rmbArray = (RmbArray) temp;
                            array = rmbArray.root;
                        } else {
                            array = (ArrayNode) temp;
                        }

                        // Busco el nodo en el arreglo
                        var node = array.getNode(RmbArray.POSITION_INITIAL, indexes);
                        // el tipo de la expresion es igual al nodo encontrado
                        this.type = node.type;
                        // Si el nodo es hoja, significa que es un valor
                        // Al contrario es un nodo arreglo
                        if (node.isValueNode()) {
                            // Si es un nodo hoja obtengo su valor
                            this.value = ((ValueNode) node).valueVar.value;
                        } else {
                            // Si es un nodo arreglo, se crea un arreglo que tendra como
                            // raiz el nodo encontrado
                            ArrayNode arrayNode = (ArrayNode) node;
                            this.value = new RmbArray((ArrayType) arrayNode.type, arrayNode);
                        }
                        return this.value;
                    } catch (IndexOutOfBoundsException e) {
                        reportError("Se ha accedido a una arreglo con un índice incorrecto. El índice es negativo o mayor o igual que el tamaño del arreglo.");
                        return null;
                    }
                } else {
                    reportError("Se ha accedido a una arreglo con un índice incorrecto. El índice es negativo o mayor o igual que el tamaño del arreglo.");
                    return null;
                }
            }
            reportError("No se puede acceder a una posicion ya que la variable " + id + " no es un arreglo.");
            return null;
        }
        reportError("No se ha declarado la variable " + id + " en este entorno.");
        return null;
    }
}
