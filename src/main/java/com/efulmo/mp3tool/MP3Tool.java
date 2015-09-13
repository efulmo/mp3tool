package com.efulmo.mp3tool;

import com.efulmo.mp3tool.command.CalculateDurationCommand;
import com.efulmo.mp3tool.command.Command;
import com.efulmo.mp3tool.command.CopyFlattenCommand;
import com.efulmo.mp3tool.command.RemoveCoverCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MP3Tool {

    private static final List<Command> availableCommands = Arrays.<Command>asList(
            new RemoveCoverCommand(),
            new CalculateDurationCommand(),
            new CopyFlattenCommand());

    public static void main(String[] args) {
        MP3Tool mp3Tool = new MP3Tool();
        List<String> arguments = Arrays.asList(args);
        Command command = mp3Tool.getCommand(arguments);

        if (command != null) {
            command.execute(arguments);
        } else {
            mp3Tool.printUsage();
        }
    }

    private void printUsage() {
        StringBuilder help = new StringBuilder();
        help.append("Usage: mp3tool <command> <arguments>\n");
        help.append("Available commands:\n");
        for (Command command : availableCommands) {
            help.append("  ");
            help.append(command.getName());
            help.append(": ");
            help.append(command.getUsage());
            help.append(System.lineSeparator());
        }

        System.out.println(help.toString());
    }

    private Command getCommand(List<String> arguments) {
        List<Command> matchesCommands = new ArrayList<Command>();
        for (Command aCommand : availableCommands) {
            if (aCommand.validateArguments(arguments)) {
                matchesCommands.add(aCommand);
            }
        }

        Command foundCommand = null;
        if (matchesCommands.size() == 1) {
            foundCommand = matchesCommands.get(0);
        }

        return foundCommand;
    }
}
