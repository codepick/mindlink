package net.codepick.commander.param.parser;

/**
 * Marker
 */
public class PrimitiveTypeParser implements FromStringParser {

    private Class<? extends Object> returnType;

    public PrimitiveTypeParser(Class<?> returnType) {
        this.returnType = returnType;
    }

    @Override
    public Object parse(String value) {
        if (returnType == String.class) {
            return value;
        } else if (returnType == byte.class || returnType == Byte.class) {
            return Byte.valueOf(value);
        } else if (returnType == short.class || returnType == Short.class) {
            return Short.valueOf(value);
        } else if (returnType == int.class || returnType == Integer.class) {
            return Integer.valueOf(value);
        } else if (returnType == long.class || returnType == Long.class) {
            return Long.valueOf(value);
        } else if (returnType == float.class || returnType == Float.class) {
            return Float.valueOf(value);
        } else if (returnType == double.class || returnType == Double.class) {
            return Double.valueOf(value);
        } else if (returnType == char.class || returnType == Character.class) {
            return value.charAt(0);
        } else if (returnType == boolean.class || returnType == Boolean.class) {
            return Boolean.valueOf(value);
        } else {
            System.out.println("ERROR: not implemented parse for type:" + returnType + " in PrimitiveTypeParser");
        }
        return null;
    }
}
