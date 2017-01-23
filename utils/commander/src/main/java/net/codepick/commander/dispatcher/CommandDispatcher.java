package net.codepick.commander.dispatcher;

import net.codepick.commander.CommandArgs;

public interface CommandDispatcher {
    void runCommand(String commandName, CommandArgs params);
}
