package com.neftxx.olc;

import com.neftxx.evaluator.Source;
import com.neftxx.util.NodeInfo;
import javafx.scene.control.TreeItem;

public class Project extends OlcNode {
    public Project(NodeInfo info) {
        super(info);
    }

    @Override
    public TreeItem<Source> getFilesForProject(String projectPath) {
        return null;
    }
}
