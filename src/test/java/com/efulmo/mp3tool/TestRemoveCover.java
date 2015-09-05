package com.efulmo.mp3tool;

import com.efulmo.mp3tool.command.RemoveCoverCommand;
import com.mpatric.mp3agic.Mp3File;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import static org.junit.Assert.assertTrue;

public class TestRemoveCover {
    
    private static final String TEST_MP3_FILE_NAME = "All My Days.mp3";
    private static final String TEST_DIR = "target/test-classes";
    private static final String TEST_DEST_DIR = "target/test-classes/test";

    @Test
    public void testCoverRemoved() throws Exception {
        prepareEnvironment();

        File file = new File(TEST_DEST_DIR, TEST_MP3_FILE_NAME);
        Mp3File mp3File = new Mp3File(file);
        assertTrue(mp3File.getId3v2Tag().getAlbumImage() != null);

        MP3Tool.main(new String[] {RemoveCoverCommand.NAME, TEST_DEST_DIR});

        file = new File(TEST_DEST_DIR, TEST_MP3_FILE_NAME);
        mp3File = new Mp3File(file);
        assertTrue(mp3File.getId3v2Tag().getAlbumImage() == null);
    }

    private void prepareEnvironment() throws IOException {
        File destDir = new File(TEST_DEST_DIR);
        destDir.mkdirs();

        File sourceFile = new File(TEST_DIR, TEST_MP3_FILE_NAME);
        File destFile = new File(TEST_DEST_DIR, TEST_MP3_FILE_NAME);
        copyFile(sourceFile, destFile);
    }

    private void copyFile(File sourceFile, File destFile) throws IOException {
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
