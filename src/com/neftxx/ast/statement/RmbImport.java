package com.neftxx.ast.statement;

import com.neftxx.ast.AstNode;
import com.neftxx.evaluator.CodeExecutor;
import com.neftxx.scope.FileScope;
import com.neftxx.scope.Scope;
import com.neftxx.util.NodeInfo;
import org.apache.commons.io.FilenameUtils;

import java.io.File;

public class RmbImport extends AstNode {
    public final String filename;

    public RmbImport(NodeInfo info, String filename) {
        super(info);
        var parent = FilenameUtils.getFullPathNoEndSeparator(info.filename);
        this.filename = FilenameUtils.concat(parent, filename);
    }

    @Override
    public FileScope interpret(Scope scope) {
        try {
            File file = new File(filename);
            return CodeExecutor.evaluate(file);
        } catch (Exception e) {
            reportError(e.getMessage());
            return null;
        }
    }
}
