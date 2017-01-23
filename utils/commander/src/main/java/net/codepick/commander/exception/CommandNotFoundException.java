package net.codepick.commander.exception;

public class CommandNotFoundException extends RuntimeException {

    private String commandName;

    public CommandNotFoundException(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
