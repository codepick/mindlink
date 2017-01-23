package net.codepick.mindlink.cli;

import net.codepick.utils.args.Args;

public class CliAppLauncher {
    public static void main(String[] args) {
        CliApplication application = new CliApplication();
        application.start(new Args(args));
    }
}
