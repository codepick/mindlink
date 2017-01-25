package net.codepick.mindlink;

import net.codepick.mindlink.utils.LaunchArgs;

public class ApplicationLauncher {
    public static void main(String[] args) {
//        args = new String[] {"addnote", "invalidPath.txt"};
//        args = new String[] {"test-theme-path"};
        args = new String[] {};
        Application application = new Application();
        application.start(new LaunchArgs(args));
    }
}
