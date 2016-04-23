package com.efulmo.mp3tool.command;

import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.Mp3File;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RemoveCoverCommand extends AbstractMp3Command {

    private class RemoveCoverCommandConfiguration extends AbstractMp3CommandConfiguration {

        public RemoveCoverCommandConfiguration(List<String> arguments) {
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

        private boolean isQuietly() {
            return getCommand().contains(QUIETLY);
        }
    }

    public static final String NAME = "remove-covers";
    public static final String CODE = "cover";
    public static final String QUIETLY = "q";
    private static final String NO_COVER_SUFFIX = ".nocover";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean validateArguments(List<String> arguments) {
        return new RemoveCoverCommandConfiguration(arguments).isValid();
    }

    @Override
    public String getUsage() {
        return CODE + "[" + QUIETLY + "]" + " <directory>";
    }

    @Override
    public void execute(List<String> arguments) {
        RemoveCoverCommandConfiguration configuration = new RemoveCoverCommandConfiguration(arguments);
        String directory = configuration.getDirectory();
        boolean quietly = configuration.isQuietly();

        removeCover(directory, quietly);
    }

    private void removeCover(String directory, boolean quiet) {
        File directoryFile = new File(directory);
        List<File> mp3Files = findMp3Files(directoryFile);
        System.out.println("MP3 files found: " + mp3Files.size());

        if (mp3Files.size() == 0) {
            return;
        }

        Collections.sort(mp3Files);
        List<File> mp3sWithCovers = retainMp3sWithCovers(mp3Files);

        System.out.println("MP3 files with covers found: " + mp3sWithCovers.size());

        if (!quiet && !mp3sWithCovers.isEmpty()) {
            System.out.println("Delete covers for all of them? (y/n)");
            String answer = getAnswer();
            if (!ANSWER_YES.equals(answer)) {
                return;
            }
        }

        removeCovers(mp3sWithCovers);
    }

    private List<File> retainMp3sWithCovers(List<File> files) {

        List<File> filesWithCovers = new ArrayList<File>();

        try {
            System.out.println("Files with covers:");
            for (File file : files) {
                Mp3File mp3File = new Mp3File(file);
                if (hasCover(mp3File)) {
                    System.out.println(file.getPath());
                    filesWithCovers.add(file);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return filesWithCovers;
    }

    private void removeCovers(List<File> files) {
        try {
            for (File file : files) {
                Mp3File mp3File = new Mp3File(file);
                if (hasCover(mp3File)) {
                    System.out.printf("%s...", file.getPath());

                    removeCover(mp3File);

                    String originFilePath = file.getPath();
                    String tempFilePath = processFileName(originFilePath);
                    mp3File.save(tempFilePath);
                    file.delete();
                    new File(tempFilePath).renameTo(file);

                    System.out.println("OK.");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean hasCover(Mp3File mp3File) {
        boolean hasCover = false;

        if (mp3File.hasId3v2Tag()) {
            byte[] image = mp3File.getId3v2Tag().getAlbumImage();
            hasCover = image != null;
        }

        return hasCover;
    }

    private void removeCover(Mp3File mp3File) {
        ID3v24Tag tag = new ID3v24Tag();
        mp3File.setId3v2Tag(tag);
        // mp3File.getId3v2Tag().clearAlbumImage();
    }

    private String processFileName(String path) {
        return path + NO_COVER_SUFFIX;
    }
}
