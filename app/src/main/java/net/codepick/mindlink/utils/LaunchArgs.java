package net.codepick.mindlink.utils;

import net.codepick.commander.CommandArgs;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class LaunchArgs {

    private String commandName;
    private CommandArgs commandArgs;

    public LaunchArgs(String...args) {
        commandName = parseCommandName(args);
        commandArgs = parseCommandParams(args);
    }

    public boolean hasCommand() {
        return StringUtils.isNotBlank(commandName);
    }

    public String getCommandName() {
        return commandName;
    }

    public CommandArgs getCommandArgs() {
        return commandArgs;
    }

    private String parseCommandName(String...args) {
        String commandName = "";
        if (args.length > 0) {
            commandName = args[0];
        }
        return commandName;
    }

    private CommandArgs parseCommandParams(String...args) {
        String [] commandParams;
        if (args.length > 1) {
            commandParams = Arrays.copyOfRange(args, 1, args.length);
        } else {
            commandParams = new String[0];
        }
        return new CommandArgs(commandParams);
    }
}
