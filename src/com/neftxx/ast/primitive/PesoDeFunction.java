package com.neftxx.ast.primitive;

import com.neftxx.ast.expression.Expression;
import com.neftxx.ast.expression.array.ArrayNode;
import com.neftxx.ast.expression.array.RmbArray;
import com.neftxx.ast.statement.fusion.Fusion;
import com.neftxx.scope.FileScope;
import com.neftxx.scope.Scope;
import com.neftxx.type.ArrayType;
import com.neftxx.type.PrimitiveType;
import com.neftxx.type.TypeTool;
import com.neftxx.type.UndefinedType;
import com.neftxx.util.NodeInfo;

public class PesoDeFunction extends Expression {
    public final String id;

    public PesoDeFunction(NodeInfo info, String id) {
        super(info);
        this.id = id;
    }

    @Override
    protected Object calculateValue(Scope scope) {
        var binding = scope.getVariable(id);
        this.type = PrimitiveType.ENT;
        if (binding != null) {
            var type = binding.type;
            if (TypeTool.isArray(type)) {
                ArrayType arrayType = (ArrayType) type;
                if (arrayType.numberDim == 1) {
                    var array = binding.valueVar.value;
                    this.value = 1;
                    if (array instanceof RmbArray) {
                        var rmbArray = (RmbArray) array;
                        this.value = rmbArray.root.childrenNodes.size();
                    } else if (array instanceof ArrayNode) {
                        var arrayNode = (ArrayNode) array;
                        this.value = arrayNode.childrenNodes.size();
                    }
                } else {
                    this.value = arrayType.numberDim;
                }
                return this.value;
            } else if (TypeTool.isFusion(type)) {
                Scope temp = (Scope) binding.valueVar.value;
                this.value = temp.variables.size();
                return this.value;
            } else if (TypeTool.isRString(type)) {
                this.value = binding.valueVar.value.toString().length();
                return this.value;
            } else {
                this.value = 1;
                return this.value;
            }
        }
        FileScope fileScope = scope.getGlobal();
        Fusion fusion = fileScope.getFusion(id);
        if (fusion != null) {
            this.value = fusion.getSize();
            return this.value;
        }
        this.type = UndefinedType.UNDEFINED;
        reportError("No se encontro alguna variable o fusion con nombre " + id + " en este archivo.");
        return null;
    }
}
