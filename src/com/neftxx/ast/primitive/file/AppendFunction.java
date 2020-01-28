package com.neftxx.ast.primitive.file;

import com.neftxx.ast.expression.Expression;
import com.neftxx.scope.Scope;
import com.neftxx.type.ArrayType;
import com.neftxx.type.PrimitiveType;
import com.neftxx.util.NodeInfo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class AppendFunction extends WriteFunction {

    public AppendFunction(NodeInfo info, Expression expression) {
        super(info, expression);
    }

    @Override
    public Object interpret(Scope scope) {
        if (WRITE_FILE == null && !ITS_OPEN) {
            var valExp = expression.interpret(scope);
            ArrayType tempType = new ArrayType(1, PrimitiveType.CHR);
            if (tempType.isAssignable(expression.type)) {
                try {
                    String cad = getCad(valExp);
                    if (cad == null) {
                        reportError("Error al obtener la cadena dada.");
                        return null;
                    }
                    File file = new File(cad);
                    if (file.isFile()) {
                        if (!file.exists()) {
                            reportError("Error abrir el archivo. El archivo no existe.");
                            return null;
                        }
                        ITS_OPEN = true;
                        WRITE_FILE = new BufferedWriter(new FileWriter(file, true));
                    } else {
                        reportError("Error al intentar abrir un archivo puede que no sea una ruta valida o puede ser un directorio.");
                    }
                    return null;
                } catch (Exception e) {
                    reportError(e.getMessage());
                    return null;
                }
            } else {
                reportError("Los tipos son incompatibles: " + expression.type + " no se puede convertir en " + tempType);
            }
        } else {
            reportError("Error ya existe un archivo abierto.");
        }
        return null;
    }
}
