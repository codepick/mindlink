package net.codepick.mindlink.command;

import net.codepick.commander.ArgValue;
import net.codepick.commander.Command;
import net.codepick.commander.CommandArgs;
import net.codepick.commander.NamedCommand;
import net.codepick.mindlink.ApplicationContext;
import net.codepick.mindlink.dao.NoteDao;
import net.codepick.mindlink.dao.ThemeDao;
import net.codepick.mindlink.dao.exception.ObjectAlreadyExistException;
import net.codepick.mindlink.domain.Note;
import net.codepick.mindlink.domain.Theme;
import net.codepick.mindlink.parser.FileNoteParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static net.codepick.mindlink.utils.IOUtils.errorMessage;
import static net.codepick.mindlink.utils.IOUtils.message;

@NamedCommand("addnote")
public class CreateNoteCommand implements Command {

    @ArgValue(index = 0)
    private String noteFilePath;

    @Override
    public void execute(CommandArgs args) {
        if (StringUtils.isBlank(noteFilePath)) {
            interactiveCreateNote();
        } else {
            createNoteByFilePath(noteFilePath);
        }
    }

    private void interactiveCreateNote() {
        message("Переход в интерактивный режим создания заметок (Stub)");
    }

    private void createNoteByFilePath(String filePath) {
        File file = new File(filePath);
        try {
            FileNoteParser fileNoteParser = new FileNoteParser();
            fileNoteParser.parseFile(file);
            String[] themePath = fileNoteParser.getThemePath();
            Note note = fileNoteParser.getNote();

            ThemeDao themeDao = ApplicationContext.get().getThemeDao();
            Theme theme = themeDao.createByNamePath(themePath);

            note.setThemeId(theme.getId());
            NoteDao noteDao = ApplicationContext.get().getNoteDao();
            noteDao.create(note);

            message("Создана заметка:");
            message(ToStringBuilder.reflectionToString(note));
        } catch (FileNotFoundException e) {
            errorMessage("Файл '%s' не найден", file.getAbsolutePath());
        } catch (IOException e) {
            errorMessage("Ошибка чтения файла '%s': %s", file.getAbsolutePath(), e.getMessage());
        } catch (ObjectAlreadyExistException e) {
            errorMessage("Заметка с таким названием для данных тем уже существует");
        }
    }
}
