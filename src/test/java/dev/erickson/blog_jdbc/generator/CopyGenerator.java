package dev.erickson.blog_jdbc.generator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

// Utility to create a Mapper skeleton
public class CopyGenerator {
    final static private String INDENT = "    ";
    private Map<String, String> methodNames;


    public void toRestWithBuilder(Class<?> clazz) throws NoSuchMethodException {
        String className = clazz.getSimpleName();
        final String source = createName(className);

        buildMethodNames(clazz);

        System.out.println("public static " + className + " toRest(final " + className + " " + source + ") {");
        System.out.println(INDENT + "var builder = " + className + ".builder();");
        System.out.print(INDENT + "return builder");

        String indent2 = INDENT + INDENT + ".";
        for (Field field : clazz.getDeclaredFields()) {
            System.out.println();
            String fieldName = field.getName();

            String getter = findGetter(fieldName);

            System.out.print(indent2 + fieldName + "(" + source + "." + getter + "())");
        }
        System.out.println();
        System.out.println(indent2 + "build();");
        System.out.println("}");
    }

    public void toRest(Class<?> clazz) throws NoSuchMethodException {
        String className = clazz.getSimpleName();
        final String source = createName(className);

        buildMethodNames(clazz);

        System.out.println("public static " + className + " toRest(final " + className + " " + source + ") {");
        System.out.print(INDENT + "return new " + className + "()");

        String indent2 = INDENT + INDENT + ".";
        for (Field field : clazz.getDeclaredFields()) {
            System.out.println();
            String fieldName = field.getName();

            String getter = findGetter(fieldName);
            String setter = findSetter(fieldName);

            System.out.print(indent2 + setter + "(" + source + "." + getter + "())");
        }
        System.out.println(";");
        System.out.println("}");
    }

    private String findGetter(final String fieldName) throws NoSuchMethodException {
        String possibleGetter = "get" + fieldName;
        String getter = methodNames.get(possibleGetter.toUpperCase());

        if (getter != null) {
            return getter;
        }
        possibleGetter = "is" + fieldName;
        getter = methodNames.get(possibleGetter.toUpperCase());

        if (getter == null) {
            throw new NoSuchMethodException("unable to locate getter for " + fieldName);
        }
        return getter;
    }

    private String findSetter(String fieldName) throws NoSuchMethodException {
        String possibleSetter = "set" + fieldName;
        String setter = methodNames.get(possibleSetter.toUpperCase());

        if (setter == null) {
            throw new NoSuchMethodException("unable to locate setter for " + fieldName);
        }
        return setter;
    }

    private void buildMethodNames(Class<?> clazz) {
        methodNames = new TreeMap<>();
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            methodNames.put(method.getName().toUpperCase(), method.getName());
        }
    }

    private String createName(final String suffix) {
        return "persist" + suffix.substring(0, 1).toUpperCase() + suffix.substring(1);
    }
}
