package net.codepick.mindlink.parser;

import net.codepick.mindlink.domain.Note;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static net.codepick.mindlink.utils.IOUtils.errorMessage;

public class FileNoteParser {

    private static final String CONFIG_FILE_TOKEN = "##";
    private static final String THEME_TOKENS_DELIMITER = "->";

    private String[] themePath;
    private Note note;

    public void parseFile(File file) throws IOException {
        if (file.isDirectory()) {
            errorMessage("Некорректный путь до файла: '%s'", file.getAbsolutePath());
            return;
        }
        try (FileReader fileReader = new FileReader(file)) {
            BufferedReader reader = new BufferedReader(fileReader);

            String line = null;
            line = getNextNotEmptyLine(reader);
            themePath = parseThemePath(line);
            line = getNextNotEmptyLine(reader);
            String noteTitle = parseNoteTitle(line);
            StringBuilder contentBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append("\r\n");
            }

            note = new Note();
            note.setTitle(noteTitle);
            note.setContent(contentBuilder.toString());
        }
    }

    private String getNextNotEmptyLine(BufferedReader reader) throws IOException {
        String line = null;
        do {
            line = reader.readLine();
            if (line == null) {
                throw new IOException("Конец файла там, где ожидалась строка");
            }
        } while (line.trim().isEmpty());
        return line;
    }

    private String[] parseThemePath(String line) throws IOException {
        line = line.trim();
        if (!line.startsWith(CONFIG_FILE_TOKEN)) {
            throw new IOException("Строка не соответствует конфигурационной строке, должна начинаться с :" + CONFIG_FILE_TOKEN);
        } else {
            line = line.replaceFirst(CONFIG_FILE_TOKEN, "");
        }
        String[] themeTokens = line.split(THEME_TOKENS_DELIMITER);
        for (int i = 0; i < themeTokens.length; i++) {
            themeTokens[i] = themeTokens[i].trim();
        }
        return themeTokens;
    }

    private String parseNoteTitle(String line) throws IOException {
        line = line.trim();
        if (!line.startsWith(CONFIG_FILE_TOKEN)) {
            throw new IOException("Строка не соответствует конфигурационной строке, должна начинаться с :" + CONFIG_FILE_TOKEN);
        } else {
            line = line.replaceFirst(CONFIG_FILE_TOKEN, "");
        }
        return line.trim();
    }


    public String[] getThemePath() {
        return themePath;
    }

    public Note getNote() {
        return note;
    }
}
