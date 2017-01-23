package net.codepick.commander.param.parser;

public interface FromStringParser <T> {
    T parse(String value);
}
