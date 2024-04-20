package org.kettingpowered.launcher.utils;

import java.io.File;

public class FileUtils {

    public static void deleteDir(File file) {
        if (!file.exists()) return;

        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children)
                    deleteDir(child);
            }
        }

        //noinspection ResultOfMethodCallIgnored
        file.delete();
    }
}
