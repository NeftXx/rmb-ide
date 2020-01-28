package com.neftxx.olc;

import com.neftxx.constant.Extension;
import com.neftxx.constant.Icons;
import com.neftxx.evaluator.Source;
import com.neftxx.util.ImageUtils;
import com.neftxx.util.NodeInfo;
import javafx.scene.control.TreeItem;
import org.apache.commons.io.FilenameUtils;

import java.io.File;

public class OlcFile extends OlcNode {
    public final String name;
    public final String date;

    public OlcFile(NodeInfo info, String name, String date) {
        super(info);
        this.name = name;
        this.date = date;
    }

    @Override
    public TreeItem<Source> getFilesForProject(String projectPath) {
        var filename = FilenameUtils.concat(projectPath, name);
        File file = new File(filename);
        if (Extension.isExtensionRmb(file.getName())) {
            return new TreeItem<>(new Source(file), ImageUtils.buildImageView(Icons.codeIconImage));
        }
        return new TreeItem<>(new Source(file), ImageUtils.buildImageView(Icons.textIconImage));
    }
}
