package com.efulmo.mp3tool.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by efulmo on 05.09.15.
 */
public abstract class AbstractMp3Command {

    protected List<File> findMp3Files(File directory) {
        List<File> mp3s = listMp3Files(directory);

        return mp3s;
    }

    protected List<File> listMp3Files(File file) {
        List<File> mp3s = new ArrayList<File>();
        if (file.isDirectory()) {
            for (File nestedFile : file.listFiles()) {
                mp3s.addAll(listMp3Files(nestedFile));
            }
        } else {
            String extension = getExtension(file.getName());
            if ("mp3".equals(extension)) {
                mp3s.add(file);
            }
        }

        return mp3s;
    }

    protected String getExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) {
            dotIndex = 0;
        }

        return fileName.substring(dotIndex + 1);
    }
}
