package net.codepick.mindlink;

import net.codepick.mindlink.command.CommandManager;

public class ApplicationContext {

    private static ApplicationContext context = new ApplicationContext();

    public static ApplicationContext get() {
        return context;
    }

    public CommandManager commandManager;

    public CommandManager getCommandManager() {
        if (commandManager == null) {
            synchronized (CommandManager.class) {
                if (commandManager == null) {
                    commandManager = new CommandManager("net.codepick.mindlink.command");
                }
            }
        }
        return commandManager;
    }

    private ApplicationContext() {
        initBaseFields();
    }

    private void initBaseFields() {

    }
}
