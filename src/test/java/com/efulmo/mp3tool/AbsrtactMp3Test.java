package com.efulmo.mp3tool;

import com.efulmo.mp3tool.Util.FileUtil;

import java.io.File;
import java.io.IOException;

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
        FileUtil.copyFile(sourceFile, destFile);
    }
}
