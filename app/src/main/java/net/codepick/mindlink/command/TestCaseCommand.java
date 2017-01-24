package net.codepick.mindlink.command;

import net.codepick.commander.Command;
import net.codepick.commander.CommandArgs;
import net.codepick.mindlink.ApplicationContext;
import net.codepick.mindlink.dao.ThemeDao;
import net.codepick.mindlink.dao.exception.ObjectNotExistException;
import net.codepick.mindlink.domain.Theme;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Arrays;

import static net.codepick.mindlink.utils.IOUtils.message;

public class TestCaseCommand implements Command {


    @Override
    public void execute(CommandArgs params) {
        testGetThemeByPath();

        testCreateThemeByPath();

        testGetThemeByPath();
    }

    private void testGetThemeByPath() {
        message("Запуск теста testGetThemeByPath");
        ThemeDao themeDao = ApplicationContext.get().getThemeDao();
        try {
            String[] themePath = {"Java", "JDBC", "ResultSet"};
            message("Запрос темы по пути:" + Arrays.toString(themePath));
            Theme theme = themeDao.getByNamePath(themePath);
            message("Тема найдена: " + ToStringBuilder.reflectionToString(theme));
        } catch (ObjectNotExistException e) {
            message("По пути не найдена тема: " + e.getObjectName());
        }
    }

    private void testCreateThemeByPath() {
        message("Запуск теста testCreateThemeByPath");
        ThemeDao themeDao = ApplicationContext.get().getThemeDao();

        String[] themePath = {"Java", "JDBC", "ResultSet"};
        message("Создание тем по пути:" + Arrays.toString(themePath));
        Theme theme = themeDao.createByNamePath(themePath);
        message("Темы созданы, последняя созданная тема: " + ToStringBuilder.reflectionToString(theme));
    }
}
