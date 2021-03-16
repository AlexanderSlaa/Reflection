package Reflection;

public class ClassReflection {

    public static boolean hasField(Class<?> objectClass, String name){
        try {
            objectClass.getDeclaredField(name);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }


}
