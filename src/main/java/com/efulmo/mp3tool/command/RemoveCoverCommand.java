package com.efulmo.mp3tool.command;

import com.mpatric.mp3agic.Mp3File;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class RemoveCoverCommand implements Command {

    public static final String NAME = "remove-cover";
    public static final String INTERACTIVELY = "interactively";
    private static final String ANSWER_YES = "y";
    private static final String NO_CCOVER_SUFFIX = ".nocover";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean validateArguments(List<String> arguments) {
        return arguments.size() == 2 || arguments.size() == 3;
    }

    @Override
    public String getUsage() {
        return "remove-cover <directory> [interactively]";
    }

    @Override
    public void execute(List<String> arguments) {
        String directory = arguments.get(1);
        boolean interactively = false;
        if (arguments.size() == 3) {
            interactively = INTERACTIVELY.equals(arguments.get(2));
        }
        removeCover(directory, interactively);
    }

    private void removeCover(String directory, boolean interactively) {
        File directoryFile = new File(directory);
        List<File> mp3Files = findMp3Files(directoryFile);
        System.out.println("MP3 files found: " + mp3Files.size());

        if (mp3Files.size() == 0) {
            return;
        }

        Collections.sort(mp3Files);
        List<File> mp3sWithCovers = retainMp3sWithCovers(mp3Files);

        System.out.println("MP3 files with covers found: " + mp3sWithCovers.size());

        if (interactively) {
            System.out.println("Delete covers for them? (y/n)");
            String answer = getAnswer();
            if (!ANSWER_YES.equals(answer)) {
                return;
            }
        }

        removeCovers(mp3sWithCovers);
    }

    private List<File> findMp3Files(File directory) {
        List<File> mp3s = listMp3Files(directory);

        return mp3s;
    }

    private List<File> listMp3Files(File file) {
        List<File> mp3s = new ArrayList<File>();
        if (file.isDirectory()) {
            for (File nestedFile : file.listFiles()) {
                mp3s.addAll(listMp3Files(nestedFile));
            }
        } else {
            String extension = getExtension(file.getName());
            if ("mp3".equals(extension)) {
                mp3s.add(file);
            }
        }

        return mp3s;
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

    private String getAnswer() {
        return new Scanner(System.in).next();
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

                    System.out.println("OK!");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) {
            dotIndex = 0;
        }

        return fileName.substring(dotIndex + 1);
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
        mp3File.getId3v2Tag().clearAlbumImage();
    }

    private String processFileName(String path) {
        return path + NO_CCOVER_SUFFIX;
    }
}
