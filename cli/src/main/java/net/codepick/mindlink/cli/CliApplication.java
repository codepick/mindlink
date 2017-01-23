package net.codepick.mindlink.cli;

import net.codepick.mindlink.cli.exception.CriticalApplicationException;
import net.codepick.utils.args.Args;

public class CliApplication {
    public void start(Args args) {
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

    private void startCommand(Args args) {
        // TODO
    }

    private void startInteractive(Args args) {
        // TODO
    }


    private void processCriticalException(CriticalApplicationException exception) {
        // TODO
    }
}
