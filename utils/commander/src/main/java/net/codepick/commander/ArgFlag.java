package net.codepick.commander;

import net.codepick.commander.param.parser.FromStringParser;
import net.codepick.commander.param.parser.PrimitiveTypeParser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ArgFlag {
    String shortName() default EMPTY;

    String longName() default EMPTY;

    String defaultValue() default EMPTY;
}
