package net.codepick.mindlink;

import net.codepick.mindlink.utils.LaunchArgs;

public class ApplicationLauncher {
    public static void main(String[] args) {
        Application application = new Application();
        application.start(new LaunchArgs(args));
    }
}
