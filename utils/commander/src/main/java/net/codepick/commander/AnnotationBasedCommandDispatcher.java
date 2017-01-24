package net.codepick.commander;

import net.codepick.commander.*;
import net.codepick.commander.exception.CommandNotFoundException;
import net.codepick.commander.exception.IllegalArgParamException;
import net.codepick.commander.exception.WrongCommandNameException;
import net.codepick.commander.param.CommandParam;
import net.codepick.commander.param.parser.FromStringParser;
import net.codepick.commander.param.parser.PrimitiveTypeParser;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationBasedCommandDispatcher implements CommandDispatcher {
    private Map<String, Class<? extends Command>> commandClassesMap = new HashMap<>();

    public AnnotationBasedCommandDispatcher() {
    }

    public AnnotationBasedCommandDispatcher(String...packagesToScan) {
        for (String packageToScan : packagesToScan) {
            addScanPackage(packageToScan);
        }
    }

    @Override
    public void runCommand(String commandName, CommandArgs params) {
        Command command = createCommandObject(commandName);
        setAnnotatedFieldValues(command, params);
        command.execute(params);
    }

    private Command createCommandObject(String commandName) {
        Class<? extends Command> commandClass = commandClassesMap.get(commandName);
        if (commandClass == null) {
            throw new CommandNotFoundException(commandName);
        }
        try {
            return commandClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void setAnnotatedFieldValues(Command command, CommandArgs params) {
        Class<? extends Command> commandClass = command.getClass();
        Field[] fields = commandClass.getDeclaredFields();
        for (Field field : fields) {
            checkAndSetArgParams(command, field, params);
            checkAndSetArgFlags(command, field, params);
            checkAndSetArgValues(command, field, params);
        }
    }

    private void checkAndSetArgParams(Command command, Field field, CommandArgs commandArgs) {
        if (!field.isAnnotationPresent(ArgParam.class)) {
            return;
        }
        ArgParam annotation = field.getAnnotation(ArgParam.class);

        String shortName = annotation.shortName();
        String longName = annotation.longName();

        if (StringUtils.isBlank(shortName) && StringUtils.isBlank(longName)) {
            throw new IllegalArgParamException(String.format("Отсутствует имя для параметра '%s'", field));
        }

        CommandParam param = new CommandParam();
        param.setShortName(shortName);
        param.setLongName(longName);
        param.setDefaultStringValue(annotation.defaultValue());

        String paramValue = commandArgs.getParamValue(param);
        if (paramValue == null && annotation.required()) {
            throw new RuntimeException(String.format("Value for param '%s' is required", field));
        } else if (paramValue == null) {
            paramValue = param.getDefaultStringValue();
        }

        if (!StringUtils.isBlank(paramValue)) {
            Class<? extends FromStringParser> parserClass = annotation.parser();
            FromStringParser parser;
            if (parserClass == PrimitiveTypeParser.class) {
                parser = new PrimitiveTypeParser(field.getType());
            } else {
                parser = getAnnotationValueParser(annotation);
            }
            Object transformedParamValue = parser.parse(paramValue);
            if (transformedParamValue != null) {
                setCommandFieldValue(command, field, transformedParamValue);
            }
        }
    }

    private void checkAndSetArgFlags(Command command, Field field, CommandArgs commandArgs) {
        if (!field.isAnnotationPresent(ArgFlag.class)) {
            return;
        }
        ArgFlag annotation = field.getAnnotation(ArgFlag.class);
        String shortName = annotation.shortName();
        String longName = annotation.longName();
        if (StringUtils.isBlank(shortName) && StringUtils.isBlank(longName)) {
            throw new IllegalArgParamException(String.format("Отсутствует имя для параметра '%s'", field));
        }
        CommandParam param = new CommandParam();
        param.setShortName(shortName);
        param.setLongName(longName);
        param.setDefaultStringValue(annotation.defaultValue());
        boolean flagValue = false;
        if (commandArgs.hasParam(param)) {
            if (!commandArgs.hasParamValue(param)) {
                flagValue = true;
            } else {
                PrimitiveTypeParser parser = new PrimitiveTypeParser(boolean.class);
                flagValue = (boolean)parser.parse(commandArgs.getParamValue(param));
            }
        }
        setCommandFieldValue(command, field, flagValue);
    }

    private void checkAndSetArgValues(Command command, Field field, CommandArgs commandArgs) {
        if (!field.isAnnotationPresent(ArgValue.class)) {
            return;
        }
        ArgValue annotation = field.getAnnotation(ArgValue.class);
        int valueIndex = annotation.index();
        if (valueIndex < 0) {
            throw new IllegalArgParamException("Индекс значения не должен быть меньше 0"); // TODO повысить информативность
        }
        if (valueIndex < commandArgs.noParamValuesSize()) {
            String value = commandArgs.getNoParamValue(valueIndex);
            setCommandFieldValue(command, field, value);
        } else if (annotation.required()) {
            throw new RuntimeException(String.format("Value for param '%s' is required", field));
        }
    }

    private FromStringParser<? super Object> getAnnotationValueParser(ArgParam annotation) {
        try {
            return annotation.parser().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Cannot create parser", e);
        }
    }

    private void setCommandFieldValue(Command command, Field field, Object value) {
        try {
            boolean accessableField = field.isAccessible();
            if (!accessableField) {
                field.setAccessible(true);
            }
            field.set(command, value);
            if (!accessableField) {
                field.setAccessible(false);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(String.format("Не удалось присвоить значение '%s' полю '%s' класса '%s'", value, field, command), e);
        }
    }

    public void addScanPackage(String packageToScan) {
        Reflections reflectionScanner = new Reflections(packageToScan);
        Set<Class<? extends Command>> executables = reflectionScanner.getSubTypesOf(Command.class);
        executables.stream().filter(commandClazz -> commandClazz.isAnnotationPresent(NamedCommand.class)).forEach(commandClazz -> {
            try {
                String name = getNameFromAnnotation(commandClazz);
                commandClassesMap.put(name, commandClazz);
            } catch (WrongCommandNameException e) {
                System.out.println("Warn: wrong command name for class" + e.getCommandClass().getCanonicalName());
            }
        });
    }

    private String getNameFromAnnotation(Class<? extends Command> commandClass) throws WrongCommandNameException {
        NamedCommand namedCommand = commandClass.getAnnotation(NamedCommand.class);
        String name = namedCommand.value();
        if (name.trim().isEmpty()) {
            throw new WrongCommandNameException(commandClass);
        }
        return name.toLowerCase();
    }
}
