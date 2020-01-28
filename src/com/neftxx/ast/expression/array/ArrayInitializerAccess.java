package com.neftxx.ast.expression.array;

import com.neftxx.ast.expression.Expression;
import com.neftxx.ast.statement.field.Dimension;
import com.neftxx.ast.util.RmbException;
import com.neftxx.scope.Scope;
import com.neftxx.type.ArrayType;
import com.neftxx.util.NodeInfo;

import java.util.ArrayList;

public class ArrayInitializerAccess extends Expression {
    public final ArrayInitializer arrayInitializer;
    public final ArrayList<Dimension> dimensions;

    public ArrayInitializerAccess(NodeInfo info, ArrayInitializer arrayInitializer) {
        super(info);
        this.arrayInitializer = arrayInitializer;
        this.dimensions = new ArrayList<>();
    }

    public void addDimension(Dimension dimension) {
        dimensions.add(dimension);
    }

    @Override
    protected Object calculateValue(Scope scope) throws RmbException {
        var root = arrayInitializer.calculateValue(scope);
        int numberDim = root.getNumberDim();
        StringBuilder builder = new StringBuilder();
        boolean isBalanced = root.verifyDimension(numberDim, builder);
        if (isBalanced) {
            int numberDimTemp = dimensions.size();
            if (numberDim >= numberDimTemp) {
                int[] indexes = new int[numberDimTemp];
                int i = 0;
                for (; i < numberDim; i ++) {
                    indexes[i] = dimensions.get(i).calculateDim(scope);
                }

                try {
                    var node = root.getNode(RmbArray.POSITION_INITIAL, indexes);
                    this.type = node.type;
                    if (node.isValueNode()) {
                        this.value = ((ValueNode) node).valueVar.value;
                    } else {
                        this.value = new RmbArray((ArrayType) this.type, (ArrayNode) node);
                    }
                    return this.value;
                } catch (IndexOutOfBoundsException e) {
                    reportError("Se ha accedido a una arreglo con un índice incorrecto. El índice es negativo o mayor o igual que el tamaño del arreglo.");
                    return null;
                }
            } else {
                reportError("Se ha accedido a una arreglo con un índice incorrecto. El índice es negativo o mayor o igual que el tamaño del arreglo.");
            }
        } else {
            String[] errors = builder.toString().split("\n");
            for(String error: errors) {
                reportError(error);
            }
        }
        return null;
    }
}
