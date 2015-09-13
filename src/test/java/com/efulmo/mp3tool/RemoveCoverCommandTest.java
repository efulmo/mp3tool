package com.efulmo.mp3tool;

import com.efulmo.mp3tool.command.RemoveCoverCommand;
import com.mpatric.mp3agic.Mp3File;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class RemoveCoverCommandTest extends AbsrtactMp3Test {

    @Test
    public void testCoverRemoved() throws Exception {
        prepareEnvironment();

        File file = new File(TEST_DEST_DIR, TEST_MP3_FILE_NAME);
        Mp3File mp3File = new Mp3File(file);
        assertTrue(mp3File.getId3v2Tag().getAlbumImage() != null);

        MP3Tool.main(new String[] {RemoveCoverCommand.NAME + RemoveCoverCommand.QUIETLY, TEST_DEST_DIR});

        file = new File(TEST_DEST_DIR, TEST_MP3_FILE_NAME);
        mp3File = new Mp3File(file);
        assertTrue(mp3File.getId3v2Tag().getAlbumImage() == null);
    }
}
