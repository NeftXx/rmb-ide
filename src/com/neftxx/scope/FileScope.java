package com.neftxx.scope;

import com.neftxx.ast.Program;
import com.neftxx.ast.gui.RmbFrame;
import com.neftxx.ast.primitive.AlDarClickFunction;
import com.neftxx.ast.primitive.IniciarVentanaFunction;
import com.neftxx.ast.statement.fusion.Fusion;
import com.neftxx.ast.statement.method.MethodStm;
import com.neftxx.type.MethodTypeFactory;
import com.neftxx.type.RmbType;
import com.neftxx.util.SymbolNode;
import org.apache.commons.io.FilenameUtils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class FileScope extends Scope {
    public static final ArrayList<SymbolNode> SYMBOL_NODES = new ArrayList<>();
    public final String filename;
    private Hashtable<String, FileScope> imports;
    public Hashtable<String, FileScope> frames;
    public Hashtable<String, AlDarClickFunction> events;
    private FusionFactory fusionFactory;
    private MethodTypeFactory methodTypeFactory;
    private MethodStm mainMethod;
    private IniciarVentanaFunction iniciarVentana;

    public FileScope(String filename) {
        super(null);
        this.filename = filename;
        this.imports = new Hashtable<>();
        this.frames = new Hashtable<>();
        this.events = new Hashtable<>();
        this.fusionFactory = new FusionFactory();
        this.methodTypeFactory = new MethodTypeFactory();
        this.mainMethod = null;
        this.iniciarVentana = null;
    }

    public static void clearSymbolsNode() {
        SYMBOL_NODES.clear();
    }

    public static void addSymbolNode(String name, String type, String scope, String rol, String parameter, String isConstant) {
        SymbolNode symbolNode = new SymbolNode(name, type, scope, rol, parameter, isConstant);
        SYMBOL_NODES.add(symbolNode);
    }

    public void setIniciarVentana(IniciarVentanaFunction iniciarVentana) {
        this.iniciarVentana = iniciarVentana;
    }

    public RmbFrame createFrame() {
        if (iniciarVentana != null) {
            Scope scope = new Scope(this);
            scope.setGlobal(this);
            return iniciarVentana.interpret(scope);
        }
        return new RmbFrame();
    }

    public boolean itAlreadyExistsIniciarVentana() {
        return iniciarVentana != null;
    }

    public boolean addEvent(String name, AlDarClickFunction function) {
        if (events.containsKey(name)) return false;
        events.put(name, function);
        return true;
    }

    public AlDarClickFunction getEvent(String id) {
        return events.get(id);
    }

    public void addImport(String filename, FileScope fileScope) {
        if (!imports.containsKey(filename)) {
            imports.put(filename, fileScope);
        }
    }

    public boolean addFrame(String filename, String name, FileScope fileScope) {
        addImport(filename, fileScope);
        if (events.containsKey(name)) return false;
        frames.put(name, fileScope);
        return true;
    }

    public FileScope getFrame(String name) {
        return frames.get(name);
    }

    public boolean addFusion(String id, Fusion fusion) {
        return fusionFactory.putFusion(id, fusion);
    }

    public Fusion getFusion(String name) {
        Fusion found = fusionFactory.getFusion(name);
        if (found == null) {
            Enumeration<FileScope> e = imports.elements();
            FileScope currentScope;
            while (e.hasMoreElements() && (found == null)) {
                currentScope = e.nextElement();
                found = currentScope.getFusion(name);
            }
        }
        return found;
    }

    public boolean addMethod(String id, RmbType type, ArrayList<RmbType> params, MethodStm method) {
        if (mainMethod == null && method.isMain()) {
            mainMethod = method;
        }
        return methodTypeFactory.putMethodType(id, type, params, method);
    }

    public void runMain() {
        if (mainMethod != null) {
            Scope scope = new Scope(this);
            scope.setGlobal(this);
            mainMethod.interpret(scope);
        }
    }

    public MethodStm getMethod(String id, ArrayList<RmbType> params) {
        MethodStm found = methodTypeFactory.getMethod(id, params);
        if (found == null) {
            Enumeration<FileScope> e = imports.elements();
            FileScope currentScope;
            while (e.hasMoreElements() && (found == null)) {
                currentScope = e.nextElement();
                found = currentScope.getMethod(id, params);
            }
        }
        return found;
    }

    @Override
    public boolean addVariable(String id, Binding binding) {
        boolean ok = super.addVariable(id, binding);
        if (ok) {
            String strScope = FilenameUtils.getName(filename);
            addSymbolNode(id, binding.type.toString(), strScope, "variable", "", Boolean.toString(binding.isConstant));
        }
        return ok;
    }

    public boolean addVariableLocal(String id, Binding binding) {
        if (variables.containsKey(id)) return false;
        variables.put(id, binding);
        return true;
    }

    public void loadImportedVariables(Program program) {
        Enumeration<FileScope> e = imports.elements();
        FileScope currentScope;
        Binding currentBinding;
        boolean notAdd;
        while (e.hasMoreElements()) {
            currentScope = e.nextElement();
            Enumeration<Binding> bindingEnumeration = currentScope.variables.elements();
            while(bindingEnumeration.hasMoreElements()) {
                currentBinding = bindingEnumeration.nextElement();
                notAdd = !addVariableLocal(currentBinding.id, currentBinding);
                if (notAdd) {
                    program.reportError("Error la variable " + currentBinding.id + " ya ha sido declarada en este entorno.");
                }
            }
        }
    }
}
