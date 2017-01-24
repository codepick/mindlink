package net.codepick.mindlink.command;

import net.codepick.commander.ArgValue;
import net.codepick.commander.Command;
import net.codepick.commander.CommandArgs;
import net.codepick.commander.NamedCommand;
import net.codepick.mindlink.ApplicationContext;
import net.codepick.mindlink.config.ConfigManager;
import net.codepick.mindlink.config.ConfigParameter;
import net.codepick.mindlink.config.exception.ConfigFileNotSavedException;

import static net.codepick.mindlink.utils.IOUtils.errorMessage;
import static net.codepick.mindlink.utils.IOUtils.message;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@NamedCommand("config")
public class ConfigCommand implements Command {

    private ConfigManager configManager = ApplicationContext.get().getConfigManager();

    @ArgValue(index = 0)
    private String configParamName;

    @ArgValue(index = 1)
    private String configEntryValue;

    @Override
    public void execute(CommandArgs args) {
        if (isNotBlank(configParamName)) {
            showOrSetParam();
        } else {
            showAllParams();
        }
    }

    private void showOrSetParam() {
        ConfigParameter parameter = configManager.getParameterByName(configParamName);
        if (parameter != null) {
            if (isNotBlank(configEntryValue)) {
                configManager.setParam(parameter, configEntryValue);
                try {
                    configManager.saveConfigToFile();
                } catch (ConfigFileNotSavedException e) {
                    errorMessage("Не удалось обновить файл конфигурации");
                }
            } else {
                String paramValue = configManager.getParamValue(parameter);
                message("'%s' = '%s'", parameter, paramValue);
            }
        } else {
            errorMessage("Параметр конфигурации '%s' не найден", configParamName);
        }
    }

    private void showAllParams() {
        message("[TODO] Все доступные параметры конфигурации:");
        for (ConfigParameter parameter : ConfigParameter.values()) {
            String paramValue = configManager.getParamValue(parameter);
            message("'%s' = '%s'", parameter, paramValue);
        }
    }

    ////////////////////////////////
//    // TODO Replace implementation
//    @Override
//    public void execute1(CommandArgs params) {
////        if (hasDbPathParam(commandArgs)) {
////            processDbPathParam(commandArgs);
////        } else if (hasSomeOtherParam(commandArgs)) {
////            setSomeOtherParam(commandArgs);
////        }
//    }
//
//    private boolean hasDbPathParam(Map<String, String> commandArgs) {
//        return commandArgs.containsKey(ConfigParameter.DatabasePath.getFieldName());
//    }
//
//    private void processDbPathParam(Map<String, String> commandArgs) {
//        String dbPathValue = commandArgs.get(ConfigParameter.DatabasePath.getFieldName());
//        if (!dbPathValue.isEmpty()) {
//            try {
//                configManager.setDbPath(dbPathValue);
//                configManager.saveConfigToFile();
//            } catch (ConfigFileNotSavedException e) {
//                errorMessage("Не удалось обновить файл конфигурации. '%s'", e.getMessage());
//            }
//            message("Установлен путь до базы данных: %s", configManager.getDbPath());
//        } else {
//            message("Путь до базы данных: %s", configManager.getDbPath());
//        }
//    }
//
//    private boolean hasSomeOtherParam(Map<String, String> commandArgs) {
//        return false; // TODO Del, Stub
//    }
//
//    private void setSomeOtherParam(Map<String, String> commandArgs) {
//        // TODO Del, Stub
//    }
}
