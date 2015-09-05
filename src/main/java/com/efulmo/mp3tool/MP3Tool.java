package com.efulmo.mp3tool;

import com.efulmo.mp3tool.command.CalculateDurationCommand;
import com.efulmo.mp3tool.command.Command;
import com.efulmo.mp3tool.command.CopyFlattenCommand;
import com.efulmo.mp3tool.command.RemoveCoverCommand;

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

        String commandString = arguments.get(0);
        Command command = mp3Tool.getCommand(commandString);
        if (command != null) {
            if (!command.validateArguments(arguments)) {
                mp3Tool.printUsage();
            };
            command.execute(arguments);
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

    private Command getCommand(String commandString) {
        Command foundCommand = null;

        for (Command aCommand : availableCommands) {
            if (commandString.equals(aCommand.getName())) {
                foundCommand = aCommand;
            }
        }

        return foundCommand;
    }
}
