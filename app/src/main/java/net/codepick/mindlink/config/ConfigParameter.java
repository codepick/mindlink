package net.codepick.mindlink.config;

public enum ConfigParameter {
    DatabasePath("databasePath");

    private final String fieldName;

    ConfigParameter(String stringValue) {
        this.fieldName = stringValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public ConfigParameter forFieldName(String fieldName) {
        for (ConfigParameter param : ConfigParameter.values()) {
            if (param.getFieldName().equals(fieldName)) {
                return param;
            }
        }
        return null;
    }
}
