package com.neftxx.ast.expression.array;

import com.neftxx.scope.ValueVar;
import com.neftxx.type.RmbType;

public class ValueNode extends ArrayNodeComponent {
    public ValueVar valueVar;

    public ValueNode(RmbType type, Object value) {
        this.type = type;
        this.valueVar = new ValueVar(value);
    }

    @Override
    public boolean isValueNode() {
        return true;
    }

    @Override
    public ArrayNodeComponent getNode(int position, int[] sizeDimensions) throws IndexOutOfBoundsException {
        return this;
    }

    @Override
    public RmbType getTypeValueNode() {
        return type;
    }

    @Override
    public boolean verifyDimension(int numberDim, StringBuilder builder) {
        boolean ok = this.numberDim == numberDim;
        if (!ok) {
            builder.append("No se puede castear el tipo ").append(this.type).append(" al ").append(this.type);
            int i = 0;
            for (; i < numberDim; i++)
                builder.append("[]");
            builder.append("\n");
        }
        return ok;
    }

    @Override
    public boolean verifyTypes(RmbType type, StringBuilder builder) {
        boolean ok = this.type.isSame(type);
        if (!ok) {
            builder.append("Los tipos son incompatibles en el arreglo: ")
                    .append(this.type).append(" no se puede convertir en ").append(type).append("\n");
        }
        return ok;
    }

    @Override
    public String toString() {
        if (valueVar != null) {
            return valueVar.toString();
        }
        return "";
    }
}
