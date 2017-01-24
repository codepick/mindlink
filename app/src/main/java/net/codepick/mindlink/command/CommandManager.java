package net.codepick.mindlink.command;

import net.codepick.commander.CommandArgs;
import net.codepick.commander.AnnotationBasedCommandDispatcher;
import net.codepick.commander.CommandDispatcher;
import net.codepick.mindlink.utils.IOUtils;

public class CommandManager {
    private CommandDispatcher commandDispatcher;

    public CommandManager(String...packagesToScan) {
        this.commandDispatcher = new AnnotationBasedCommandDispatcher(packagesToScan);
    }

    public void runCommand(String commandName, CommandArgs commandArgs) {
        commandDispatcher.runCommand(commandName, commandArgs);
    }

    public static void main(String[] args) {
        CommandDispatcher cs = new AnnotationBasedCommandDispatcher("net.codepick.mindlink.command");
        args = new String[] {"hello world", "-s", "-t=TestType from cmd", "--find=findSsTRING", "some test", "second val"};

        cs.runCommand("test", new CommandArgs(args));

        IOUtils.message("Тестирование команды config");
        CommandManager manager = new CommandManager("net.codepick.mindlink.command");
        manager.runCommand("config", new CommandArgs("databasepath", "someval"));
    }
}
