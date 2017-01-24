package net.codepick.mindlink.interactive.state;

import java.util.Stack;

public abstract class State {
    private Stack<State> stateStack;

    public State(Stack<State> stateStack) {
        this.stateStack = stateStack;
    }

    public abstract void execute();

    protected Stack<State> getStateStack() {
        return stateStack;
    }
}
