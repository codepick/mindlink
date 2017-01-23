package net.codepick.commander;

import net.codepick.commander.param.parser.FromStringParser;
import net.codepick.commander.param.parser.PrimitiveTypeParser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ArgValue {
    int index();

    boolean required() default false;

    Class<? extends FromStringParser> parser() default PrimitiveTypeParser.class;
}