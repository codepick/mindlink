package net.codepick.mindlink.cli.command;

import net.codepick.commander.CommandArgs;
import net.codepick.commander.dispatcher.AnnotationBasedCommandDispatcher;
import net.codepick.commander.dispatcher.CommandDispatcher;

public class CommandManager {

    private CommandDispatcher commandDispatcher;

    public void runCommand(String commandName, CommandArgs commandArgs) {
        commandDispatcher.runCommand(commandName, commandArgs);
    }

    public static void main(String[] args) {
        CommandDispatcher cs = new AnnotationBasedCommandDispatcher("net.codepick.mindlink.cli.command");
        args = new String[] {"hello world", "-s=true", "-t=TestType from cmd"};

        cs.runCommand("test", new CommandArgs(args));
    }
}
