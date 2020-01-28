package com.neftxx.ast.expression.array;

import com.neftxx.type.ArrayType;
import com.neftxx.type.RmbType;
import com.neftxx.type.UndefinedType;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ArrayNode extends ArrayNodeComponent {
    public final ArrayList<ArrayNodeComponent> childrenNodes;

    public ArrayNode(ArrayList<ArrayNodeComponent> childrenNodes) {
        this.childrenNodes = childrenNodes;
    }

    public void initialize(int numberDim, int currentIndex, int[] indexes, RmbType type, Object value) {
        int i;
        this.numberDim = numberDim;
        if (currentIndex == numberDim) {
            for (i = 0; i < indexes[currentIndex - 1]; i++) {
                childrenNodes.add(new ValueNode(type, value));
            }
            return;
        }
        for (i = 0; i < indexes[currentIndex - 1]; i++) {
            ArrayNode child = new ArrayNode();
            child.type = new ArrayType(numberDim - currentIndex + 1, type);
            childrenNodes.add(child);
            child.initialize(numberDim, currentIndex + 1, indexes, type, value);
        }
    }

    public ArrayNode() {
        this(new ArrayList<>());
    }

    public void calculateDim() {
            if (childrenNodes.isEmpty()) {
                this.numberDim = 1;
                return;
            }
            this.numberDim = childrenNodes.get(0).getNumberDim();
            for (var child: childrenNodes) {
                if (this.numberDim < child.getNumberDim())
                    this.numberDim = child.getNumberDim();
            }
            this.numberDim++;
    }

    public void setNode(int position, int [] indexes, ArrayNode node) throws IndexOutOfBoundsException {
        if (position == (indexes.length - 1)) {
            childrenNodes.set(indexes[position], node);
            return;
        }
        ArrayNodeComponent child = childrenNodes.get(indexes[position]);
        if (child instanceof ArrayNode) {
            setNode(position + 1, indexes, (ArrayNode) child);
        }
    }

    public boolean verifySizeOfDimensions(int position, int[] indexes, StringBuilder builder) throws IndexOutOfBoundsException {
        int size = indexes[position];

        boolean ok = size == childrenNodes.size();
        if (!ok) {
            builder.append("Error la posicion ").append(position).append(" es de tamaño ")
                    .append(size).append(" pero la inicializacion del arreglo es de tamaño ")
                    .append(childrenNodes.size()).append(" en este tipo de declaracion los tamaño deben ser iguales").append("\n");
        }

        if (position == (indexes.length - 1)) {
            return ok;
        }
        ArrayNodeComponent child = childrenNodes.get(size - 1);
        if (child instanceof ArrayNode) {
            ArrayNode arrayNode = (ArrayNode) child;
            return ok && arrayNode.verifySizeOfDimensions(position + 1, indexes, builder);
        }
        return false;
    }

    public void concat(ArrayNode arrayNode) {
        childrenNodes.addAll(arrayNode.childrenNodes);
    }

    @Override
    public boolean isValueNode() {
        return false;
    }

    @Override
    public ArrayNodeComponent getNode(int position, int[] indexes) throws ArrayIndexOutOfBoundsException {
        if (position == indexes.length) {
            return this;
        }
        ArrayNodeComponent child = childrenNodes.get(indexes[position]);
        return child.getNode(position + 1, indexes);
    }

    @Override
    public RmbType getTypeValueNode() {
        for(var node: childrenNodes) {
            return  node.getTypeValueNode();
        }
        return UndefinedType.UNDEFINED;
    }

    @Override
    public boolean verifyDimension(int numberDim, StringBuilder builder) {
        if (this.numberDim != numberDim) {
            ArrayType type = (ArrayType) this.type;
            builder.append("Los tipos son incompatibles: ").append(type.type);
            int i = 0;
            for (; i < numberDim; i++)
                builder.append("[]");
            builder.append(" no se puede convertir en ").append(this.type).append("\n");
            return false;
        }
        var ret = new AtomicBoolean(true);
        for (var node: childrenNodes)
            ret.set(ret.get() && node.verifyDimension(numberDim - 1, builder));
        return ret.get();
    }

    @Override
    public boolean verifyTypes(RmbType type, StringBuilder builder) {
        var ret = new AtomicBoolean(true);
        this.type = new ArrayType(this.getNumberDim(), type);
        for (var child: childrenNodes)
            ret.set(ret.get() && child.verifyTypes(type, builder));
        return ret.get();
    }

    @Override
    public String toString() {
        if (childrenNodes != null) {
            return " {" + childrenNodes +  "} ";
        }
        return " {} ";
    }
}
