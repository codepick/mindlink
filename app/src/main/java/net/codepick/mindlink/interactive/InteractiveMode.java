package net.codepick.mindlink.interactive;

import net.codepick.mindlink.interactive.state.MenuState;
import net.codepick.mindlink.interactive.state.State;
import net.codepick.mindlink.utils.LaunchArgs;

import java.util.Stack;

public class InteractiveMode {

    private Stack<State> stateStack = new Stack<>();

    public void start(LaunchArgs args) {
        stateStack.push(new MenuState(stateStack));

        while (!stateStack.isEmpty()) {
            State currentState = stateStack.peek();
            currentState.execute();
        }
    }
}
