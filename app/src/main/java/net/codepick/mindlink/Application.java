package net.codepick.mindlink;

import net.codepick.commander.exception.CommandNotFoundException;
import net.codepick.mindlink.exception.CriticalApplicationException;
import net.codepick.mindlink.utils.IOUtils;
import net.codepick.mindlink.utils.LaunchArgs;

import static net.codepick.mindlink.utils.IOUtils.message;

public class Application {
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
        try {
            ApplicationContext.get().getCommandManager().runCommand(args.getCommandName(), args.getCommandArgs());
        } catch (CommandNotFoundException e) {
            IOUtils.errorMessage("Команда '%s' не найдена", args.getCommandName());
        }
    }

    private void startInteractive(LaunchArgs args) {
        // TODO Impl
        message("[TODO] Интерактивный режим");
    }


    private void processCriticalException(CriticalApplicationException exception) {
        // TODO Impl
        message("[TODO] Обработка критической ошибки приложения: %s", exception);
    }
}
