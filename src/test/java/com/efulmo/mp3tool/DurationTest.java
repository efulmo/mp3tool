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
public class DurationTest extends AbsrtactMp3Test {

    @Test
    public void testDuration() throws Exception {
        PrintStream oldSout = System.out;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        MP3Tool.main(new String[]{CalculateDurationCommand.NAME, TEST_DIR});

        String result = out.toString();
        assertThat(result, containsString("0:9:54"));
    }
}
