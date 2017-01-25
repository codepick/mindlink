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
}
