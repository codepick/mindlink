package net.codepick.mindlink.cli.command;

import net.codepick.commander.ArgParam;
import net.codepick.commander.Command;
import net.codepick.commander.CommandArgs;
import net.codepick.commander.NamedCommand;
import net.codepick.commander.param.parser.TestType;
import net.codepick.commander.param.parser.TestTypeParser;

@NamedCommand("test")
public class TestCommand implements Command {

    @ArgParam(shortName = "s")
    private boolean showInfo;

    @ArgParam(shortName = "f", longName = "find", defaultValue = "noneAnno", required = true)
    private String findString = "none";

    @ArgParam(shortName = "n", longName = "number", required = true, defaultValue = "12123.12313")
    private double number;

    @ArgParam(shortName = "t", longName = "testtype", defaultValue = "Test in TestType", parser = TestTypeParser.class)
    private TestType testType;

//    @ArgValue(index = 0)
//    private String path;

    public TestCommand() {

    }

    @Override
    public void execute(CommandArgs args) {
        System.out.println("Test command ");
        System.out.println("show info=" + showInfo);
        System.out.println("Find string=" + findString);
        System.out.println("number=" + number);

        System.out.println(testType + "\r\n" + testType.getValue());
    }
}