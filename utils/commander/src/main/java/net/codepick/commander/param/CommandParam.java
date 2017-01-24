package net.codepick.commander.param;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class CommandParam {
    private String longName;
    private String shortName;
    private String description;
    private String defaultStringValue;

    public boolean hasLongName() {
        return !isBlank(longName);
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public boolean hasShortName() {
        return !isBlank(shortName);
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDefaultStringValue() {
        return defaultStringValue;
    }

    public void setDefaultStringValue(String defaultStringValue) {
        this.defaultStringValue = defaultStringValue;
    }
}