package com.efulmo.mp3tool.command;

import com.mpatric.mp3agic.Mp3File;

import java.io.File;
import java.util.List;

/**
 * Created by efulmo on 05.09.15.
 */
public class CalculateDurationCommand extends AbstractMp3Command {

    private class CalculateDurationConfiguration extends AbstractMp3CommandConfiguration {

        public CalculateDurationConfiguration(List<String> arguments) {
            super(arguments);
        }

        @Override
        public boolean isValid() {
            return isArgumentCountMatched() && isCommandNameMatches();
        }

        @Override
        protected boolean isArgumentCountMatched() {
            return arguments.size() == 2;
        }

        @Override
        protected boolean isCommandNameMatches() {
            return getCommand().contains(CODE);
        }

        private String getCommand() {
            return arguments.get(0);
        }

        private String getDirectory() {
            return arguments.get(1);
        }
    }

    public static final String NAME = "calculate-duration";
    public static final String CODE = "duration";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean validateArguments(List<String> arguments) {
        return new CalculateDurationConfiguration(arguments).isValid();
    }

    @Override
    public String getUsage() {
        return CODE + " <directory>";
    }

    @Override
    public void execute(List<String> arguments) {
        CalculateDurationConfiguration configuration = new CalculateDurationConfiguration(arguments);
        String directory = configuration.getDirectory();
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

        return String.format("%dh %dm %ds", hours, minutes, seconds);
    }
}
