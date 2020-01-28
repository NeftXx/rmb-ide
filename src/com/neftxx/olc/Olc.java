package com.neftxx.olc;

import com.neftxx.evaluator.Source;
import com.neftxx.util.NodeInfo;
import javafx.scene.control.TreeItem;

public class Olc {
    public final NodeInfo info;
    public final OlcProject project;

    public Olc(NodeInfo info, OlcProject project) {
        this.info = info;
        this.project = project;
    }

    public String getRunPath() {
        return project.run;
    }

    public TreeItem<Source> getFilesForProject(String absolutePath) {
        return project.getFilesForProject(absolutePath);
    }
}
