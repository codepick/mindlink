package net.codepick.mindlink.command;

import net.codepick.commander.Command;
import net.codepick.commander.CommandArgs;
import net.codepick.commander.exception.CommandNotFoundException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// TODO run command with params, check
public class DynamicCreateNoteCommand implements Command {

    @Override
    public void execute(CommandArgs params) {
        File file = new File(UUID.randomUUID().toString() + ".tmpnote");
        try {
            String canonicalPath = file.getCanonicalPath();
            Runtime.getRuntime().exec("subl " + canonicalPath);

            while (true) {
                if (file.exists()) {
                    Map<String, String> map = new HashMap<>();
                    map.put(canonicalPath, null);
//                    ApplicationContext.get().getCommandManager().runCommand("addnote", new map);
                    file.delete();
                    break;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException | CommandNotFoundException e) {
            e.printStackTrace();
        }
    }
}
