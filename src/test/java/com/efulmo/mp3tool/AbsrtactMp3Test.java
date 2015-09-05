package com.efulmo.mp3tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by efulmo on 05.09.15.
 */
public abstract class AbsrtactMp3Test {

    protected static final String TEST_MP3_FILE_NAME = "All My Days.mp3";
    protected static final String TEST_DIR = "target/test-classes";
    protected static final String TEST_DEST_DIR = "target/test-classes/test";

    protected void prepareEnvironment() throws IOException {
        File destDir = new File(TEST_DEST_DIR);
        destDir.mkdirs();

        File sourceFile = new File(TEST_DIR, TEST_MP3_FILE_NAME);
        File destFile = new File(TEST_DEST_DIR, TEST_MP3_FILE_NAME);
        copyFile(sourceFile, destFile);
    }

    protected void copyFile(File sourceFile, File destFile) throws IOException {
        if(!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        }
        finally {
            if(source != null) {
                source.close();
            }
            if(destination != null) {
                destination.close();
            }
        }
    }
}
