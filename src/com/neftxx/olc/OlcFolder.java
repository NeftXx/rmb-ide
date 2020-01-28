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

public class OlcFolder extends OlcNode {
    public final String name;
    public final ArrayList<OlcNode> files;

    public OlcFolder(NodeInfo info, String name, ArrayList<OlcNode> files) {
        super(info);
        this.name = name;
        this.files = files;
    }

    @Override
    public TreeItem<Source> getFilesForProject(String projectPath) {
        var currentDirectory = FilenameUtils.concat(projectPath, name);
        File file = new File(currentDirectory);
        var root = new TreeItem<>(new Source(file), ImageUtils.buildImageView(Icons.folderIconImage));
        for (var node: files) {
            var treeItem = node.getFilesForProject(currentDirectory);
            if (Objects.nonNull(treeItem)) {
                root.getChildren().add(treeItem);
            }
        }
        return root;
    }
}
