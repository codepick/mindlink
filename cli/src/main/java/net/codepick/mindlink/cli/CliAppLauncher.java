package net.codepick.mindlink.cli;

public class CliAppLauncher {
    public static void main(String[] args) {
        CliApplication application = new CliApplication();
        application.start(new LaunchArgs(args));
    }
}
