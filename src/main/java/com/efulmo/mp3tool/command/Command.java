package com.efulmo.mp3tool.command;

import java.util.List;

/**
 * Created by efulmo on 05.09.15.
 */
public interface Command {

    String getName();
    boolean validateArguments(List<String> arguments);
    String getUsage();
    void execute(List<String> arguments);
}
