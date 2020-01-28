package com.neftxx.ast.expression.array;

import com.neftxx.type.ArrayType;
import com.neftxx.type.RmbType;
import com.neftxx.type.TypeTool;

public class ArrayFactory {
    public static RmbArray createArray(int numberDim, int[] indexes, RmbType type) {
        Object value = null;
        if (TypeTool.isBul(type)) value = false;
        else if (TypeTool.isEnt(type)) value = 0;
        else if (TypeTool.isDec(type)) value = 0.0;
        else if (TypeTool.isChr(type)) value = '\0';
        else if (TypeTool.isRString(type)) value = "";

        ArrayNode root = new ArrayNode();
        ArrayType arrayType = new ArrayType(numberDim, type);
        root.type = arrayType;
        root.initialize(numberDim, 1, indexes, type, value);
        root.calculateDim();
        return new RmbArray(arrayType, root);
    }
}
