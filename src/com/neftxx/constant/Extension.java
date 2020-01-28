package com.neftxx.constant;

import org.apache.commons.io.FilenameUtils;

public class Extension {
    public static final String[] R_FILE = {"r", "R"};
    public static final String[] M_FILE = {"m", "M"};
    public static final String[] B_FILE = {"b", "B"};
    public static final String[] OLC_FILE = {"olc", "OLC"};

    public static boolean isExtensionRmb(String filename) {
        return isFileR(filename) || isFileM(filename) || isFileB(filename);
    }

    public static boolean isFileR(String filename) {
        return FilenameUtils.isExtension(filename, R_FILE);
    }

    public static boolean isFileM(String filename) {
        return FilenameUtils.isExtension(filename, M_FILE);
    }

    public static boolean isFileB(String filename) {
        return FilenameUtils.isExtension(filename, B_FILE);
    }

    public static boolean isOlcFile(String filename) {
        return FilenameUtils.isExtension(filename, OLC_FILE);
    }
}
