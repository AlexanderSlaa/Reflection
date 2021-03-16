package Reflection;

import java.lang.reflect.Field;

public class InstanceReflection {

    public static <T> T get(Object instance, String fieldName,Class<T> returnType){
        try {
            return parse(get(instance,instance.getClass().getDeclaredField(fieldName)), returnType);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static <T> T parse(Object fieldValue,Class<T> returnType){
        return (T)fieldValue;
    }

    public static Class<?> getType(Object o, String fieldName){
        try {
            return o.getClass().getDeclaredField(fieldName).getType();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T get(Object instance, String fieldName){
        try {
            Field f = instance.getClass().getDeclaredField(fieldName);
            return (T) get(instance,fieldName, f.getType());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Object get(Object instance, Field f){
        f.setAccessible(true);
        try {
            return f.get(instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
