package Reflection.ObjectEditor;

import Reflection.*;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static Reflection.Reflection.*;

public class ObjectEditor implements EditWatchdog{

    public static <T> T field(T object, String fieldName, Object fieldValue) throws ReflectionException {
        disableWarning();
        try {
            Field[] fields = Search(object.getClass(), fieldName);
            if(fields.length == 0){
                throw new ReflectionException("Can't find field in class or underlying classes");
            }else if(fields.length > 1){
                System.err.println("WARNING: more than 1 field found fields are matched by type class");
            }
            for (Field f : fields) {
                if(f.getType().equals(fieldValue.getClass())){
                    if(EditWatchdog.Accept(f,f.getAnnotation(Editor.class), before())) {
                        f.set(object, fieldValue);
                        return object;
                    }else{
                        throw new IllegalAccessException("Can't access field in object because accessing class not registered");
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new ReflectionException(e);
        }
        return object;
    }

    public static Field[] Search(Class<?> objectClass, String name){
        ArrayList<Field> fields = new ArrayList<>();
        Class<?> focused = objectClass;
        while (focused != Object.class){
            for (Field field : focused.getDeclaredFields()) {
                if(field.getName().equals(name)){
                    fields.add(field);
                }
            }
            focused = focused.getSuperclass();
        }
        return fields.toArray(Field[]::new);
    }

    public static Object value(Object instance, String ... path) throws NoSuchFieldException, IllegalAccessException {
        disableWarning();
        for (String fieldName : path) {
            Field[] fields = instance.getClass().getDeclaredFields();
            Field f = instance.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            instance = f.get(instance);
        }
        return instance;
    }

    public static void disableWarning() {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            Unsafe u = (Unsafe) theUnsafe.get(null);

            Class cls = Class.forName("jdk.internal.module.IllegalAccessLogger");
            Field logger = cls.getDeclaredField("logger");
            u.putObjectVolatile(cls, u.staticFieldOffset(logger), null);
        } catch (Exception e) {
            // ignore
        }
    }


}
