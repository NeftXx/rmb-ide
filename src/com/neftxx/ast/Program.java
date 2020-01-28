package com.neftxx.ast;

import com.neftxx.ast.primitive.AlDarClickFunction;
import com.neftxx.ast.primitive.GuiFunction;
import com.neftxx.ast.primitive.IniciarVentanaFunction;
import com.neftxx.ast.statement.RmbImport;
import com.neftxx.ast.statement.field.VarDeclaration;
import com.neftxx.ast.statement.fusion.Fusion;
import com.neftxx.ast.statement.method.MethodStm;
import com.neftxx.constant.Extension;
import com.neftxx.scope.FileScope;
import com.neftxx.scope.Scope;
import com.neftxx.type.RmbType;
import com.neftxx.util.NodeInfo;
import org.apache.commons.io.FilenameUtils;

import java.util.ArrayList;
import java.util.Objects;

public class Program extends AstNode {
    public final FileScope fileScope;
    public final ArrayList<AstNode> nodes;
    private String strScope;

    public Program(NodeInfo info, ArrayList<AstNode> nodes) {
        super(info);
        this.fileScope = new FileScope(info.filename);
        this.fileScope.setGlobal(this.fileScope);
        this.nodes = nodes;
        strScope = FilenameUtils.getName(info.filename);
    }

    public FileScope createFileScope() {
        resolveImports();
        saveObjects();
        saveMethods();
        saveFields();
        if (Extension.isFileB(info.filename)) saveGuiFunctions();
        return fileScope;
    }

    private void resolveImports() {
        ArrayList<AstNode> deleteFoundNodes = new ArrayList<>();
        for (var node: Objects.requireNonNull(nodes)) {
            if (node instanceof RmbImport) {
                RmbImport rmbImport = (RmbImport) node;
                String filename = info.filename;
                if (!rmbImport.filename.equals(filename)) {
                    if (Extension.isFileR(filename)) {
                        if (Extension.isFileR(rmbImport.filename)) {
                            reportError("Error no se puede importar un archivo con extension R en otro archivo con extension R.");
                        } else if (Extension.isFileM(rmbImport.filename)) {
                            FileScope scope = rmbImport.interpret(null);
                            if (scope != null) {
                                fileScope.addImport(scope.filename, scope);
                            }
                        } else if (Extension.isFileB(rmbImport.filename)) {
                            FileScope scope = rmbImport.interpret(null);
                            if (scope != null) {
                                String name = FilenameUtils.getBaseName(scope.filename);
                                boolean notAdd = !fileScope.addFrame(scope.filename, name, scope);
                                if (notAdd) {
                                    reportError("Error el ya existe un formulario con el nombre " + name);
                                }
                            }
                        } else {
                            reportError("Error no se pudo reconocer la extension del archivo importado.");
                        }
                    } else if (Extension.isFileM(filename) || Extension.isFileB(filename)) {
                        if (Extension.isFileM(rmbImport.filename)) {
                            FileScope scope = rmbImport.interpret(null);
                            if (scope != null) {
                                fileScope.addImport(scope.filename, scope);
                            }
                        } else {
                            reportError("Error no se puede importar un archivo con esta extension en un archivo con extension M.");
                        }
                    } else {
                        reportError("Error no se pudo reconocer la extension del archivo importado.");
                    }
                } else {
                    reportError("No se puede importar el mismo archivo actual.");
                }
                deleteFoundNodes.add(node);
            }
        }
        nodes.removeAll(deleteFoundNodes);
    }

    private void saveObjects() {
        ArrayList<AstNode> deleteFoundNodes = new ArrayList<>();
        for (var node : nodes) {
            if (node instanceof Fusion) {
                Fusion fusion = (Fusion) node;
                boolean notVerifyVariableNames = !fusion.verifyVariableNames();
                if (notVerifyVariableNames) {
                    boolean notAddFusion = !fileScope.addFusion(fusion.id, fusion);
                    if (notAddFusion) {
                        fusion.reportError("El fusion con nombre " + fusion.id + " ya esta declarado en este archivo.");
                    } else {
                        FileScope.addSymbolNode(fusion.id, "", strScope, "fusion", "", "false");
                    }
                }
                deleteFoundNodes.add(node);
            }
        }
        nodes.removeAll(deleteFoundNodes);
    }

    private void saveMethods() {
        ArrayList<AstNode> deleteFoundNodes = new ArrayList<>();
        for (var node : Objects.requireNonNull(nodes)) {
            if (node instanceof MethodStm) {
                MethodStm method = (MethodStm) node;
                ArrayList<RmbType> typesParams = new ArrayList<>();
                for (var parameter: method.parameters) {
                    typesParams.add(parameter.type);
                }
                if (method.isMain() && !Extension.isFileR(info.filename)) {
                    reportError("No puede existir un metodo principal en este tipo de archivo.");
                } else {
                    method.setMyFileScope(this.fileScope);
                    boolean notAdd = !fileScope.addMethod(method.id, method.type, typesParams, method);
                    if (notAdd) {
                        method.reportError("El metodo " + method.id + " ya existe en este archivo.");
                    } else {
                        FileScope.addSymbolNode(method.id, method.type.toString(), strScope, "metodo",
                                typesParams.toString(), "false");
                    }
                }
                deleteFoundNodes.add(node);
            }
        }
        nodes.removeAll(deleteFoundNodes);
    }

    private void saveFields() {
        ArrayList<AstNode> deleteFoundNodes = new ArrayList<>();
        fileScope.loadImportedVariables(this);
        for (var node: Objects.requireNonNull(nodes)) {
            if (node instanceof VarDeclaration) {
                node.interpret(fileScope);
                deleteFoundNodes.add(node);
            }
        }
        nodes.removeAll(deleteFoundNodes);
    }

    private void saveGuiFunctions() {
        for(var node: nodes) {
            if (node instanceof GuiFunction) {
                var val = node.interpret(null);
                if (val != null) {
                    if (val instanceof IniciarVentanaFunction) {
                        if (fileScope.itAlreadyExistsIniciarVentana()) {
                            reportError("Error ya existe la funcion iniciar ventana en este archivo.");
                        } else {
                            fileScope.setIniciarVentana((IniciarVentanaFunction) val);
                        }
                    } else if (val instanceof AlDarClickFunction) {
                        var gui = (GuiFunction) node;
                        boolean notAdd = !fileScope.addEvent(gui.id1, (AlDarClickFunction) val);
                        if (notAdd) {
                            reportError("Error ya existe un evento al dar click para el boton " + gui.id1);
                        }
                    }
                }
            }
        }
    }
    @Override
    public Object interpret(Scope scope) {
        return null;
    }
}
