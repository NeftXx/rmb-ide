package com.neftxx.ast.primitive.file;

import com.neftxx.ast.expression.Expression;
import com.neftxx.scope.Scope;
import com.neftxx.type.ArrayType;
import com.neftxx.type.PrimitiveType;
import com.neftxx.type.TypeTool;
import com.neftxx.util.NodeInfo;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class ReadFunction extends WriteFunction {
    public final String id;

    public ReadFunction(NodeInfo info, Expression expression, String id) {
        super(info, expression);
        this.id = id;
    }

    @Override
    public Object interpret(Scope scope) {
        if (WRITE_FILE == null && !ITS_OPEN) {
            var binding = scope.getVariable(id);
            if (binding != null) {
                if (TypeTool.isRString(binding.type)) {
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
                                WRITE_FILE = null;
                                binding.valueVar.value = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                            } else {
                                reportError("Error al intentar abrir un archivo puede que no sea una ruta valida o puede ser un directorio.");
                            }
                        }  catch (Exception e) {
                            reportError(e.getMessage());
                            return null;
                        }
                    } else {
                        reportError("Los tipos son incompatibles: " + expression.type + " no se puede convertir en " + tempType);
                    }
                } else {
                    reportError("Error la variable pasada al metodo read no es de tipo Rstring.");
                }
            } else {
                reportError("La variable " + id + " no ha sido declarada.");
            }
        } else {
            reportError("Error ya existe un archivo abierto.");
        }
        return null;
    }
}
