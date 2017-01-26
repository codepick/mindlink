package net.codepick.mindlink.interactive.state;

import net.codepick.commander.CommandArgs;
import net.codepick.commander.exception.CommandNotFoundException;
import net.codepick.mindlink.ApplicationContext;

import java.util.Stack;

public class AddNoteState extends State {

    public AddNoteState(Stack<State> stateStack) {
        super(stateStack);
    }

    @Override
    public void execute() {
        





        try {
            ApplicationContext.get().getCommandManager().runCommand("addnote", new CommandArgs());
        } catch (CommandNotFoundException e) {
            e.printStackTrace();
        }
        getStateStack().pop();
    }
}
