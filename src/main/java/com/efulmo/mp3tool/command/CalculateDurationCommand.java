package com.efulmo.mp3tool.command;

import com.mpatric.mp3agic.Mp3File;

import java.io.File;
import java.util.List;

/**
 * Created by efulmo on 05.09.15.
 */
public class CalculateDurationCommand extends AbstractMp3Command implements Command {

    public static final String NAME = "calculate-duration";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean validateArguments(List<String> arguments) {
        return arguments.size() == 2;
    }

    @Override
    public String getUsage() {
        return NAME + " <directory>";
    }

    @Override
    public void execute(List<String> arguments) {
        String directory = arguments.get(1);
        calculateDuration(directory);
    }

    private void calculateDuration(String directory) {
        File directoryFile = new File(directory);
        List<File> mp3Files = findMp3Files(directoryFile);
        System.out.println("MP3 files found: " + mp3Files.size());

        long durationInSeconds = calculateDuration(mp3Files);
        System.out.println("Total duration: " + prettyDuration(durationInSeconds));
    }

    private long calculateDuration(List<File> mp3Files) {
        long duration = 0;
        try {
            for (File file : mp3Files) {
                Mp3File mp3File = new Mp3File(file);
                long mp3Duration = mp3File.getLengthInSeconds();
                duration += mp3Duration;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return duration;
    }

    private String prettyDuration(long durationInSeconds) {
        long seconds = durationInSeconds;
        long minutes = 0;
        long hours = 0;

        if (seconds > 59) {
            minutes = seconds / 60;
            seconds = seconds % 60;
        }

        if (minutes > 59) {
            hours = minutes / 60;
            minutes = minutes % 60;
        }

        return String.format("%d:%d:%d", hours, minutes, seconds);
    }
}
