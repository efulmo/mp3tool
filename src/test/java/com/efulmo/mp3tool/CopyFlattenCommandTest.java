package com.efulmo.mp3tool;

import com.efulmo.mp3tool.util.FileUtil;
import com.efulmo.mp3tool.command.CopyFlattenCommand;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by efulmo on 06.09.15.
 */
public class CopyFlattenCommandTest extends AbsrtactMp3Test {

    @Test
    public void testCopy() throws Exception {
        File destDir = new File(TEST_DEST_DIR);
        FileUtil.delete(destDir);
        assertFalse(destDir.exists());

        MP3Tool.main(new String[] {CopyFlattenCommand.NAME + CopyFlattenCommand.QUIETLY, TEST_DIR, TEST_DEST_DIR});
        File destFile = new File(TEST_DEST_DIR, TEST_MP3_FILE_NAME);
        assertTrue(destFile.exists());
    }
}
