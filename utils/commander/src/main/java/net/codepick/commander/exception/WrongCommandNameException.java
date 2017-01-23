package net.codepick.commander.exception;

import net.codepick.commander.Command;

public class WrongCommandNameException extends RuntimeException {
    private Class<? extends Command> commandClass;

    public WrongCommandNameException(Class<? extends Command> commandClass) {
        this.commandClass = commandClass;
    }

    public Class<? extends Command> getCommandClass() {
        return commandClass;
    }
}
