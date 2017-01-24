package net.codepick.commander;

import net.codepick.commander.CommandArgs;

public interface CommandDispatcher {
    void runCommand(String commandName, CommandArgs params);
}
