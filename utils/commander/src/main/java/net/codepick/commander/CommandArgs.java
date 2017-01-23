package net.codepick.commander;

import javafx.util.Pair;
import net.codepick.commander.param.CommandParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class CommandArgs {
    private static final java.lang.String EQUAL_SIGN = "=";

    private Map<String, String> shortParamsMap = new HashMap<>();
    private Map<String, String> longParamsMap = new HashMap<>();
    private List<String> noParamValues = new ArrayList<>();

    public CommandArgs(String... commandArgs) {
        parseArgs(commandArgs);
    }

    public boolean isParamPresent(CommandParam param) {
        return shortParamsMap.containsKey(param.getShortName()) || longParamsMap.containsKey(param.getLongName());
    }

    public boolean isParamValuePresent(CommandParam param) {
        return getParamValue(param) != null;
    }

    public String getParamValue(CommandParam param) {
        if (param.hasShortName()) {
            String value = shortParamsMap.get(param.getShortName());
            if (value != null) {
                return value;
            }
        } else if (param.hasLongName()) {
            String value = longParamsMap.get(param.getLongName());
            if (value != null) {
                return value;
            }
        }
        return param.getDefaultStringValue();
    }

    public int noParamValuesSize() {
        return noParamValues.size();
    }

    public boolean hasNoParamValues() {
        return noParamValuesSize() > 0;
    }

    public String getNoParamValue(int index) {
        return noParamValues.get(index);
    }

    private void parseArgs(String[] tokens) {
        for (String token : tokens) {
            if (token.startsWith("--")) {
                Pair<String, String> keyValue = parseTokenWithPrefix(token, "--");
                longParamsMap.put(keyValue.getKey(), keyValue.getValue());
            } else if (token.startsWith("-")) {
                Pair<String, String> keyValue = parseTokenWithPrefix(token, "-");
                shortParamsMap.put(keyValue.getKey(), keyValue.getValue());
            } else {
                noParamValues.add(token);
            }
        }
    }

    private Pair<String, String> parseTokenWithPrefix(String token, String prefix) {
        token = token.replaceFirst(prefix, EMPTY);
        String[] keyValue = token.split(EQUAL_SIGN, 2);
        String key = keyValue[0];
        String value = keyValue.length > 1 ? keyValue[1] : null;
        return new Pair<>(key, value);
    }

    // TEST: TODO Move to test classes
    public static void main(String[] args) {

    }

    public static void testParseTokenWithPrefix() {
        CommandArgs args = new CommandArgs();
        String token = "--hello=world=dsfsdf=asdasdasd";
        Pair<String, String> pair = args.parseTokenWithPrefix(token, "--");
        System.out.println("key: '" + pair.getKey() + "'");
        System.out.println("value: '" + pair.getValue() + "'");
    }
}
