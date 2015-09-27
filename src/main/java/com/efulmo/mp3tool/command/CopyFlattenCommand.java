package com.efulmo.mp3tool.command;

import com.efulmo.mp3tool.util.FileUtil;

import java.io.File;
import java.util.List;

/**
 * Created by efulmo on 05.09.15.
 */
public class CopyFlattenCommand extends AbstractMp3Command {

    private class CopyFlattenCommandConfiguration extends AbstractMp3CommandConfiguration {

        public CopyFlattenCommandConfiguration(List<String> arguments) {
            super(arguments);
        }

        @Override
        public boolean isValid() {
            return isArgumentCountMatched() && isCommandNameMatches();
        }

        @Override
        protected boolean isArgumentCountMatched() {
            return arguments.size() == 3;
        }

        @Override
        protected boolean isCommandNameMatches() {
            return getCommand().contains(CODE);
        }

        private String getCommand() {
            return arguments.get(0);
        }

        private String getFromDirectory() {
            return arguments.get(1);
        }

        private String getToDirectory() {
            return arguments.get(2);
        }

        private boolean isQuietly() {
            return getCommand().contains(QUIETLY);
        }
    }

    public static final String NAME = "copy-flatten";
    public static final String CODE = "copy";
    public static final String QUIETLY = "q";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean validateArguments(List<String> arguments) {
        return new CopyFlattenCommandConfiguration(arguments).isValid();
    }

    @Override
    public String getUsage() {
        return CODE + "[" + QUIETLY + "]" + " <from> <to>";
    }

    @Override
    public void execute(List<String> arguments) {
        CopyFlattenCommandConfiguration configuration = new CopyFlattenCommandConfiguration(arguments);

        String from = configuration.getFromDirectory();
        String to = configuration.getToDirectory();
        boolean quietly = configuration.isQuietly();

        copyFlatten(from, to, quietly);
    }

    private void copyFlatten(String from, String to, boolean quietly) {
        File fromDirectory = new File(from);
        List<File> files = findMp3Files(fromDirectory);
        System.out.println("Total MP3 files to copy: " + files.size());

        long totalSize = 0;
        if (!files.isEmpty()) {
            totalSize = calculateTotalSize(files);
            System.out.println("Total size of files to be copied: " + prettySize(totalSize));
        }

        if (!quietly && !files.isEmpty()) {
            System.out.println("Copy them? (y/n)");
            String answer = getAnswer();
            if (!ANSWER_YES.equals(answer)) {
                return;
            }
        }

        copyFiles(files, to);
    }

    private long calculateTotalSize(List<File> files) {
        long totalSize = 0;

        for (File file : files) {
            totalSize += file.length();
        }

        return totalSize;
    }

    private void copyFiles(List<File> files, String to) {
        new File(to).mkdirs();

        for (File file : files) {
            File destFile = new File(to, file.getName());

            System.out.printf("Copying %s...", file.getPath());
            FileUtil.copyFileSilently(file, destFile);
            System.out.println("OK.");
        }
    }

    private String prettySize(long size) {
        long b = size;
        long kb = 0;
        long mb = 0;
        long gb = 0;

        if (b > 1023) {
            kb = b / 1024;
            b = b % 1024;
        }

        if (kb > 1023) {
            mb = kb / 1024;
            kb = kb % 1024;
        }

        if (mb > 1023) {
            gb = mb / 1024;
            mb = mb % 1024;
        }

        return String.format("%dG %dM %dk %db", gb, mb, kb, b);
    }
}
