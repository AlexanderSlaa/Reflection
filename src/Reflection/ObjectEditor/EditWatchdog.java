package Reflection.ObjectEditor;

import java.lang.reflect.Field;

public interface EditWatchdog {

    static boolean Accept(Field field, Editor editor, Class<?> accessor){
        for (Class<?> aClass : editor.accessor()) {
            if(aClass.equals(Void.class)){
                field.setAccessible(true);
                return true;
            }else if(aClass.equals(accessor)){
                field.setAccessible(true);
                return true;
            }
        }
        return false;
    }

}
