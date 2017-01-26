package net.codepick.mindlink.interactive.state;

import net.codepick.commander.CommandArgs;
import net.codepick.commander.exception.CommandNotFoundException;
import net.codepick.mindlink.ApplicationContext;

import java.util.Stack;

import static net.codepick.mindlink.utils.IOUtils.getInput;
import static net.codepick.mindlink.utils.IOUtils.message;

public class MenuState extends State {

    public MenuState(Stack<State> stateStack) {
        super(stateStack);
    }

    @Override
    public void execute() {
        message("");
        message("===== ГЛАВНОЕ МЕНЮ ======");
        message("1 : Навигация по заметкам");
        message("2 : Добавить заметку");
        message("3 : Выход");
        message("");
        String input = getInput("Введите номер:");
        switch (input) {
            case "1":
                getStateStack().push(new NoteNavigationState(getStateStack()));
                break;
            case "2":
                getStateStack().push(new AddNoteState(getStateStack()));
                break;
            case "3":
                getStateStack().pop();
                break;

            default:
                message("Некорректный номер");
        }
    }
}
