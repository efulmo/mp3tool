package com.efulmo.mp3tool;

import com.efulmo.mp3tool.command.CalculateDurationCommand;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

/**
 * Created by efulmo on 05.09.15.
 */
public class DurationCommandTest extends AbsrtactMp3Test {

    @Test
    public void testDuration() throws Exception {
        prepareEnvironment();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        MP3Tool.main(new String[]{CalculateDurationCommand.CODE, TEST_DEST_DIR});

        String result = out.toString();
        assertThat(result, containsString("0:4:57"));
    }
}
