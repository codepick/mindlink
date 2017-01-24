package net.codepick.mindlink.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class IOUtils {

    private static final String ERROR_MESSAGE_PREFIX = "Ошибка: ";
    private static PrintStream out;
    private static BufferedReader in;

    static {
        initDefaultIO();
    }

    public static void print(String line) {
        out.print(line);
    }

    public static void print(String line, Object... args) {
        out.printf(line, args);
    }

    public static void println(String line) {
        out.println(line);
    }

    public static void println(String line, Object... args) {
        out.println(String.format(line, args));
    }

    public static void message(String line) {
        println(line);
    }

    public static void message(String line, Object... args) {
        println(line, args);
    }

    public static void errorMessage(String line) {
        message("%s %s", ERROR_MESSAGE_PREFIX, line);
    }

    public static void errorMessage(String line, Object... args) {
        errorMessage(String.format(line, args));
    }

    public static String getInput() {
        try {
            return in.readLine().trim();
        } catch (IOException e) {
            errorMessage("Невозможно выполнить чтение строки: %s", e.getMessage());
            return "";
        }
    }

    public static String getInput(String prefixMessage) {
        print(prefixMessage);
        return getInput();
    }

    private static void initDefaultIO() {
        out = System.out;
        in = new BufferedReader(new InputStreamReader(System.in));
    }
}