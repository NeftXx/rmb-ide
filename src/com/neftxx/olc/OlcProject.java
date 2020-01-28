package com.neftxx.olc;

import com.neftxx.constant.Icons;
import com.neftxx.evaluator.Source;
import com.neftxx.util.ImageUtils;
import com.neftxx.util.NodeInfo;
import javafx.scene.control.TreeItem;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class OlcProject {
    public final NodeInfo info;
    public String route, name, run;
    public final ArrayList<OlcNode> olcNodes;

    public OlcProject(NodeInfo info, String route, String name, String run, ArrayList<OlcNode> olcNodes) {
        this.info = info;
        this.route = route.trim();
        this.name = name.trim();
        this.run = run.trim();
        this.olcNodes = olcNodes;
    }

    public TreeItem<Source> getFilesForProject(String absolutePath) {
        if (!this.run.isEmpty()) {
            String name = FilenameUtils.getName(this.run);
            if (!this.name.isEmpty()) {
                this.route = FilenameUtils.getFullPathNoEndSeparator(info.filename);
                this.run = FilenameUtils.concat(this.route, name);
            }
        }
        File file = new File(absolutePath);
        TreeItem<Source> root = new TreeItem<>(new Source(file.getParentFile()));
        root.setGraphic(ImageUtils.buildImageView(Icons.folderIconImage));
        String parent = file.getParent();
        for (var nodes: olcNodes) {
            var treeItem = nodes.getFilesForProject(parent);
            if (Objects.nonNull(treeItem)) {
                root.getChildren().add(treeItem);
            }
        }
        return root;
    }
}
