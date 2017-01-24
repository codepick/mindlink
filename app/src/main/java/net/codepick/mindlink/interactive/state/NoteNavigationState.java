package net.codepick.mindlink.interactive.state;

import net.codepick.mindlink.ApplicationContext;
import net.codepick.mindlink.dao.NoteDao;
import net.codepick.mindlink.dao.ThemeDao;
import net.codepick.mindlink.domain.Note;
import net.codepick.mindlink.domain.Theme;
import net.codepick.mindlink.utils.IOUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static net.codepick.mindlink.utils.IOUtils.message;

public class NoteNavigationState extends State {
    private final NoteDao noteDao;
    private final ThemeDao themeDao;

    private Long currentThemeId = ThemeDao.ROOT_THEME_ID;
    private List<String> themePath = new ArrayList<>();

    public NoteNavigationState(Stack<State> stateStack) {
        super(stateStack);
        noteDao = ApplicationContext.get().getNoteDao();
        themeDao = ApplicationContext.get().getThemeDao();
    }

    @Override
    public void execute() {
        List<Theme> themes = themeDao.getChildrenById(currentThemeId);
        List<Note> notes = noteDao.getByTheme(currentThemeId);

        message("");
        message("======== ЗАМЕТКИ =========");
        if (!themePath.isEmpty()) {
            StringBuilder sb =  new StringBuilder();
            for (String themeTitle : themePath) {
                sb.append(themeTitle).append(" -> ");
            }
            message(sb.toString());
        }

        int backVariantShift = 0;
        int themeVariantsShift = themes.size();

        message("0 : .. Назад");
        int variantNumber = 1;
        for (int i = 0; i < themes.size(); i++) {
            Theme theme = themes.get(i);
            message("%d : [Тема] %s", variantNumber++, theme.getTitle());
        }
        for (int i = 0; i < notes.size(); i++) {
            Note note = notes.get(i);
            message("%d : [Заметка] %s", variantNumber++, note.getTitle());
        }
        
        String input = IOUtils.getInput("Номер: ");
        if (input.isEmpty()) {
            moveBack();
        } else if ("q".equalsIgnoreCase(input)) {
            getStateStack().pop();
        }else {
            try {
                int intInput = Integer.parseInt(input);
                if (intInput == 0) {
                    moveBack();
                } else if (!themes.isEmpty()
                        && intInput > backVariantShift
                        && intInput <= (backVariantShift + themes.size())) {
                    int selectedThemeNumber = intInput - backVariantShift - 1;
                    Theme selectedTheme = themes.get(selectedThemeNumber);
                    navigateToTheme(selectedTheme);
                } else if (!notes.isEmpty()
                        && intInput > backVariantShift + themeVariantsShift
                        && intInput <= (backVariantShift + themeVariantsShift + notes.size())) {
                    int selectedNoteNumber = intInput - backVariantShift - themeVariantsShift - 1;
                    Note selectedNote = notes.get(selectedNoteNumber);
                    showSelectedNote(selectedNote);
                }
            } catch (NumberFormatException e) {
                IOUtils.errorMessage("Неверный формат ввода");
            }
        }
    }

    private void moveBack() {
        if (ThemeDao.ROOT_THEME_ID.equals(currentThemeId)) {
            getStateStack().pop();
        } else {
            // TODO Hack. Переделать так, чтобы DAO возвращал некую тему, у которой нулевой id
            Theme currentTheme = themeDao.getById(currentThemeId);
            currentThemeId = currentTheme.getParentId();
            themePath.remove(themePath.size() - 1);
        }
    }

    private void navigateToTheme(Theme theme) {
        currentThemeId = theme.getId();
        themePath.add(theme.getTitle());
    }

    private void showSelectedNote(Note note) {
        message("======== ЗАМЕТКА =========");
        message(note.getTitle());
        message("========= ТЕКСТ ==========");
        message(note.getContent());
    }
}
