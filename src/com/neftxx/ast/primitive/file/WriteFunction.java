package com.neftxx.ast.primitive.file;

import com.neftxx.ast.AstNode;
import com.neftxx.ast.expression.Expression;
import com.neftxx.ast.expression.array.ArrayNode;
import com.neftxx.ast.expression.array.RmbArray;
import com.neftxx.ast.util.StringAnalyzer;
import com.neftxx.scope.Scope;
import com.neftxx.type.ArrayType;
import com.neftxx.type.PrimitiveType;
import com.neftxx.util.NodeInfo;
import org.apache.commons.io.FilenameUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WriteFunction extends AstNode {
    public final Expression expression;

    public WriteFunction(NodeInfo info, Expression expression) {
        super(info);
        this.expression = expression;
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
                    if (!file.exists()) {
                        boolean ok = file.createNewFile();
                        if (!ok) {
                            reportError("Error no se pudo crear el archivo.");
                            return null;
                        }
                    }
                    ITS_OPEN = true;
                    WRITE_FILE = new BufferedWriter(new FileWriter(file, false));
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

    public String getCad(Object valExp) {
        String cad;
        if (valExp instanceof RmbArray) {
            cad = StringAnalyzer.obtenerCadena(((RmbArray) valExp).root);
        } else if (valExp instanceof ArrayNode) {
            cad = StringAnalyzer.obtenerCadena((ArrayNode) valExp);
        } else {
            return null;
        }

        Path myPath = Paths.get(cad);
        if (!myPath.isAbsolute()) {
            var parent = FilenameUtils.getFullPathNoEndSeparator(info.filename);
            cad = FilenameUtils.concat(parent, cad);
        }
        return cad;
    }
}
