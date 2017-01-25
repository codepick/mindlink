package net.codepick.mindlink;

import net.codepick.mindlink.command.CommandManager;
import net.codepick.mindlink.config.ConfigManager;
import net.codepick.mindlink.dao.NoteDao;
import net.codepick.mindlink.dao.ThemeDao;
import net.codepick.mindlink.dao.sqlite.SqliteNoteDao;
import net.codepick.mindlink.dao.sqlite.SqliteThemeDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static net.codepick.mindlink.utils.IOUtils.message;

public class ApplicationContext {

    private static final Logger log = LoggerFactory.getLogger(ApplicationContext.class);

    private static ApplicationContext context = new ApplicationContext();

    public static ApplicationContext get() {
        return context;
    }

    private ConfigManager configManager;
    private CommandManager commandManager;
    private DataSource dataSource;
    private ThemeDao themeDao; // LAZY
    private NoteDao noteDao; // LAZY

    public CommandManager getCommandManager() {
        if (commandManager == null) {
            synchronized (CommandManager.class) {
                if (commandManager == null) {
                    commandManager = new CommandManager("net.codepick.mindlink.command");
                }
            }
        }
        return commandManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public void close() {
        if (dataSource != null) {
            try {
                dataSource.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private ApplicationContext() {
        initBaseFields();
    }

    private void initBaseFields() {
        log.info("Init base context objects");
        configManager = new ConfigManager();
    }

    public NoteDao getNoteDao() {
        if (noteDao == null) {
            synchronized (NoteDao.class) {
                if (noteDao == null) {
                    noteDao = new SqliteNoteDao(getDataSource());
                }
            }
        }
        return noteDao;
    }

    /**
     * Lazy init // TODO
     *
     * @return
     */
    public ThemeDao getThemeDao() {
        if (themeDao == null) {
            synchronized (ThemeDao.class) {
                if (themeDao == null) {
                    themeDao = new SqliteThemeDao(getDataSource());
                }
            }
        }
        return themeDao;
    }

    // TODO Вынести в отдельные классы
    private DataSource getDataSource() {
        if (dataSource == null) {
            synchronized (this) {
                if (dataSource == null) {
                    String dbPath = getConfigManager().getDbPath();

                    DriverManagerDataSource dataSource = new DriverManagerDataSource();
                    dataSource.setDriverClassName("org.sqlite.JDBC");
                    dataSource.setUsername("digest");
                    dataSource.setPassword("digest");
                    dataSource.setUrl("jdbc:sqlite:" + dbPath);
                    this.dataSource = dataSource;

                    File databaseFile = new File("./" + dbPath);
                    if (!databaseFile.exists()) { // TODO Вынести в методы инициализации
                        message("Не найдена база данных: '%s'", databaseFile.getAbsolutePath());
                        createDatabase(dataSource);
                    } else {
                        message("Используется база данных: '%s'", databaseFile.getAbsolutePath());
                    }
                }
            }
        }
        return dataSource;
    }

    private void createDatabase(DataSource dataSource) {
        message("Инициализация новой базы данных...");
        try {
            List<String> createStatements = getDatabaseCreateStatement();
            for (String statement : createStatements) {
                dataSource.getConnection().prepareStatement(statement).execute();
            }
            message("База данных успешно проинициализирована");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // TODO Возвращаю список строк, т.к. одной строкой несколько выражений не выполняются, починить
    private List<String> getDatabaseCreateStatement() {
        List<String> statements = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        InputStream fileStream = getClass().getClassLoader().getResourceAsStream("database/create_database.sql");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(fileStream))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    sb.append(line).append("\r\n");
                } else {
                    String statement = sb.toString();
                    statements.add(statement);
                    sb.delete(0, sb.length() - 1);
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("Скрипт создания новой базы данных:");
                statements.forEach(log::debug);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return statements;
    }

    // -- TODO Вынести в отдельные классы
}
