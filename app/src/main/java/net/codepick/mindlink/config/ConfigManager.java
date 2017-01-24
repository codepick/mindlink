package net.codepick.mindlink.config;

import net.codepick.mindlink.config.exception.ConfigFileNotSavedException;
import net.codepick.mindlink.config.exception.ConfigFileParseException;
import net.codepick.mindlink.config.exception.ConfigParameterParseException;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static net.codepick.mindlink.utils.IOUtils.errorMessage;
import static net.codepick.mindlink.utils.IOUtils.message;

public class ConfigManager {

    private static final String CONFIG_FILE_NAME = "config.properties";
    private static final String DEFAULT_DB_PATH = "notes.db";

    private String dbPath;

    public ConfigManager() {
        try {
            loadFromFile();
        } catch (FileNotFoundException | ConfigFileParseException e) {
            createDefaultConfig();
        } catch (ConfigParameterParseException e) {
            errorMessage("Некорректные параметры конфигурации:");
            Map<String, String> wrongParams = e.getWrongParamsMap();
            for (String paramName : wrongParams.keySet()) {
                errorMessage("* Параметр: '%s', Значение: '%s'", paramName, wrongParams.get(paramName));
            }
            // TODO Поведение при некорректных значениях аргументов
        }
    }

    public String getDbPath() {
        return dbPath;
    }

    public void setDbPath(String dbPath) {
        this.dbPath = dbPath;
    }

    public void saveConfigToFile() throws ConfigFileNotSavedException {
        File configFile = new File(CONFIG_FILE_NAME);
        if (configFile.exists()) {
            boolean deleted = configFile.delete();
            if (!deleted) {
                throw new ConfigFileNotSavedException("Не удалось перезаписать файл конфигурации");
            }
        }
        try (Writer writer = new FileWriter(configFile)) {
            Properties properties = getPropertiesFromConfig();
            properties.store(writer, null);
        } catch (IOException e) {
            throw new ConfigFileNotSavedException(e.getMessage());
        }

    }

    private void loadFromFile() throws FileNotFoundException, ConfigFileParseException, ConfigParameterParseException {
        File configFile = new File(CONFIG_FILE_NAME);
        if (!configFile.exists() || !configFile.isFile()) {
            throw new FileNotFoundException();
        }
        try (Reader reader = new FileReader(configFile)) {
            Properties properties = new Properties();
            properties.load(reader);
            setConfigFromProperties(properties);
        } catch (IOException e) {
            throw new ConfigFileParseException(e.getMessage());
        }
    }

    private void setConfigFromProperties(Properties properties) throws ConfigParameterParseException {
        Map<String, String> errorParams = new HashMap<>();
        String dbPath = properties.getProperty(ConfigParameter.DatabasePath.getFieldName());
        if (!StringUtils.isBlank(dbPath)) {
            setDbPath(dbPath);
        } else {
            errorParams.put(ConfigParameter.DatabasePath.getFieldName(), "пусто");
        }

        if (!errorParams.isEmpty()) {
            throw new ConfigParameterParseException(errorParams);
        }
    }

    private void createDefaultConfig() {
        message("Создается новый конфигурационный файл");
        try {
            loadDefaultConfig();
            saveConfigToFile();
        } catch (ConfigFileNotSavedException e) {
            errorMessage("Невозможно создать новый конфигурационный файл: '%s'", e.getMessage());
        }
    }

    private void loadDefaultConfig() {
        setDbPath(DEFAULT_DB_PATH);
    }

    private Properties getPropertiesFromConfig() {
        Properties properties = new Properties();
        properties.put(ConfigParameter.DatabasePath.getFieldName(), dbPath);
        return properties;
    }
}
