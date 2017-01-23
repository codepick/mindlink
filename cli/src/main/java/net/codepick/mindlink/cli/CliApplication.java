package net.codepick.mindlink.cli;

import net.codepick.mindlink.cli.command.CommandManager;
import net.codepick.mindlink.cli.exception.CriticalApplicationException;

public class CliApplication {
    private CommandManager commandManager;

    public void start(LaunchArgs args) {
        try {
            if (args.hasCommand()) {
                startCommand(args);
            } else {
                startInteractive(args);
            }
        } catch (CriticalApplicationException e) {
            processCriticalException(e);
        }
    }

    private void startCommand(LaunchArgs args) {
        commandManager.runCommand(args.getCommandName(), args.getCommandArgs());
    }

    private void startInteractive(LaunchArgs args) {
        // TODO
    }


    private void processCriticalException(CriticalApplicationException exception) {
        // TODO
    }
}
