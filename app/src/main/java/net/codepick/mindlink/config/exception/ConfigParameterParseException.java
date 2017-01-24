package net.codepick.mindlink.config.exception;

import java.util.Map;

public class ConfigParameterParseException extends Exception {

    private Map<String, String> wrongParamNames;


    public ConfigParameterParseException(Map<String, String> wrongParamNames) {
        this.wrongParamNames = wrongParamNames;
    }

    public Map<String, String> getWrongParamsMap() {
        return wrongParamNames;
    }
}
