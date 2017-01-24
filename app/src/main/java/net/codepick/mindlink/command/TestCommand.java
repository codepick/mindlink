package net.codepick.mindlink.command;

import net.codepick.commander.*;
import net.codepick.commander.param.parser.TestType;
import net.codepick.commander.param.parser.TestTypeParser;

@NamedCommand("test")
public class TestCommand implements Command {

    @ArgParam(shortName = "s")
    private boolean showInfo;

    @ArgParam(shortName = "f", longName = "find", defaultValue = "noneAnno")
    private String findString = "none";

    @ArgParam(shortName = "n", longName = "number", defaultValue = "12123.12313")
    private double number;

    @ArgParam(shortName = "t", longName = "testtype", defaultValue = "Test in TestType", parser = TestTypeParser.class)
    private TestType testType;

    @ArgValue(index = 0)
    private String path;

    @ArgValue(index = 2)
    private String secondNoParam;

    public TestCommand() {

    }

    @Override
    public void execute(CommandArgs args) {
        System.out.println("Test command ");
        System.out.println("show info=" + showInfo);
        System.out.println("Find string=" + findString);
        System.out.println("number=" + number);
        System.out.println("path=" + path);
        System.out.println("secondNoParam=" + secondNoParam);

        System.out.println(testType + "\r\n" + testType.getValue());
    }
}