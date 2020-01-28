package com.neftxx.ast.statement.field;

import com.neftxx.ast.AstNode;
import com.neftxx.ast.expression.array.ArrayFactory;
import com.neftxx.scope.Binding;
import com.neftxx.scope.Scope;
import com.neftxx.type.ArrayType;
import com.neftxx.type.RmbType;
import com.neftxx.type.TypeTool;
import com.neftxx.util.NodeInfo;

import java.util.ArrayList;

public class VarDeclaration extends AstNode {
    public final RmbType type;
    public final String id;
    protected final ArrayList<Dimension> dimensions;

    public VarDeclaration(NodeInfo info, RmbType type, String id) {
        super(info);
        this.type = type;
        this.id = id;
        this.dimensions = new ArrayList<>();
    }

    public void addDimension(Dimension dimension) {
        dimensions.add(dimension);
    }

    public int getNumberDimensions() {
        return dimensions.size();
    }

    @Override
    public Object interpret(Scope scope) {
        Object value = null;
        int numberDim = getNumberDimensions();
        if (numberDim == 0) {
            if (TypeTool.isBul(type)) value = false;
            else if (TypeTool.isDec(type)) value = 0.00;
            else if (TypeTool.isEnt(type)) value = 0;
            else if (TypeTool.isChr(type)) value = '\0';
            else if (TypeTool.isRString(type)) value = "";
            scope.addVariable(id, new Binding(id, type, value, false));
        } else {
            try {
                int[] indexes = new int[numberDim];
                int i = 0;
                for (; i < numberDim; i ++) {
                    indexes[i] = dimensions.get(i).calculateDim(scope);
                }
                ArrayType arrayType = new ArrayType(numberDim, type);
                var array = ArrayFactory.createArray(numberDim, indexes, type);
                scope.addVariable(id, new Binding(id, arrayType, array, false));
            } catch (Exception e) {
                reportError(e.getMessage());
            }
        }
        return null;
    }
}
