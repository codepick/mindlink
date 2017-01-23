package net.codepick.commander.param.parser;

public class TestTypeParser implements FromStringParser<TestType> {
    @Override
    public TestType parse(String value) {
        TestType test = new TestType();
        test.setValue(value);
        return test;
    }
}
