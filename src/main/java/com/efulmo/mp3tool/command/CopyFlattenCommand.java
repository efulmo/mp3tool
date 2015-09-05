package com.efulmo.mp3tool.command;

import com.efulmo.mp3tool.Util.FileUtil;

import java.io.File;
import java.util.List;

/**
 * Created by efulmo on 05.09.15.
 */
public class CopyFlattenCommand extends AbstractMp3Command {

    public static final String NAME = "copy-flatten";
    public static final String INTERACTIVELY = "interactively";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean validateArguments(List<String> arguments) {
        return arguments.size() == 3 || arguments.size() == 4;
    }

    @Override
    public String getUsage() {
        return NAME + "<from> <to>";
    }

    @Override
    public void execute(List<String> arguments) {
        String from = arguments.get(1);
        String to = arguments.get(2);
        boolean interactively = false;
        if (arguments.size() == 4) {
            interactively = INTERACTIVELY.equals(arguments.get(3));
        }

        copyFlatten(from, to, interactively);
    }

    private void copyFlatten(String from, String to, boolean interactively) {
        File fromDirectory = new File(from);
        List<File> files = findMp3Files(fromDirectory);
        System.out.println("Total MP3 files to copy: " + files.size());

        long totalSize = calculateTotalSize(files);
        System.out.println("Total size of files to be copied: " + prettySize(totalSize));

        if (interactively) {
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
