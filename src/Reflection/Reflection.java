package Reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Reflection {

    private static StackTraceElement On() throws ReflectionException.ShadowException {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for (int i = 1; i < elements.length; i++) {
            if (!elements[i].getClassName().equals(Reflection.class.getName())) {
                return elements[i];
            }
        }
        throw new ReflectionException.ShadowException("Can't get element to reflect");
    }

    private static StackTraceElement On(Class<?> objectClass) throws ReflectionException.ShadowException {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for (int i = 1; i < elements.length; i++) {
            if (elements[i].getClassName().equals(objectClass.getName())) {
                return elements[i];
            }
        }
        throw new ReflectionException.ShadowException("Can't get element to reflect");
    }

    private static StackTraceElement Before(Class<?> objectClass) throws ReflectionException.ShadowException {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for (int i = 1; i < elements.length; i++) {
            if (!elements[i].getClassName().equals(objectClass.getName()) && !elements[i].getClassName().equals(Reflection.class.getName()) ) {
                return elements[i];
            }
        }
        throw new ReflectionException.ShadowException("Can't get element to reflect");
    }

    public static Method Method(Class<?> objectClass, Object ... objects) throws ReflectionException {
        return METHOD(On(objectClass), toClassArray(objects));
    }

    public static Method Method(Object ... objects) throws ReflectionException {
        return METHOD(On(), toClassArray(objects));
    }

    //region hide
    public static <T extends Annotation> T Annotation(Class<T> annotationClass, Object ... classes) throws ReflectionException {
        return Method(classes).getAnnotation(annotationClass);
    }

    public static <T extends Annotation> T Annotation(Class<?> objectClass, Class<T> annotationClass, Object ... objects) throws ReflectionException {
        return METHOD(Reflection.On(objectClass), toClassArray(objects)).getAnnotation(annotationClass);
    }
    
    public static Annotation[] Annotations() throws ReflectionException {
        return Method().getAnnotations();
    }

    public static Class<?> Class() throws ReflectionException {
        return CLASS(Reflection.On());
    }

    public static HashMap<String, Object> Statics(Object instance)
    {
        HashMap<String, Object> staticMap = new HashMap<>();
        for (Field field :  instance.getClass().getDeclaredFields()) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                try {
                    field.setAccessible(true);
                    staticMap.put(field.getName(), field.get(instance));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return staticMap;
    }

    public static Class<?> before() throws ReflectionException {
        return CLASS(Reflection.Before(Reflection.Class()));
    }

    public static <T> Constructor<T> getNonParamsConstructor(Class<T> objectClass){
        for (Constructor<?> constructor : objectClass.getDeclaredConstructors()) {
            if(constructor.getParameters().length == 0){
                return (Constructor<T>) constructor;
            }
        }
        return null;
    }

    public static <T> T createInstance(Class<T> objectClass) throws ReflectionException{
        Constructor<T> c = getNonParamsConstructor(objectClass);
        if(c != null){
            try {
                return c.newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new ReflectionException(e);
            }
        }
        throw new ReflectionException("Can't find non params required construcotr");
    }

    public static class ReflectionException extends Throwable {
        public ReflectionException(String message) {
            super(message);
        }

        public ReflectionException(Throwable throwable) {
            super(throwable);
        }

        public static class ShadowException extends ReflectionException {
            public ShadowException(String message) {
                super(message);
            }
        }
    }
    //endregion

    private static ArrayList<Class<?>>toClassArray(Object ... objects){
         ArrayList<Class<?>> classArrayList = new ArrayList<Class<?>>();
         if(objects != null) {
             for (int i = 0; i < objects.length; i++) {
                 if (objects[i] instanceof Class<?>) {
                     classArrayList.add((Class<?>) objects[i]);
                 } else if (objects[i] != null) {
                     classArrayList.add(objects[i].getClass()); //TODO: LETOP MEERDE METHODES KUNNEN HIERDOOR MACHEND WORDEN !!!!
                 }
             }
         }
        return classArrayList;
    }

    private static Method METHOD(StackTraceElement element, ArrayList<Class<?>> classArrayList) throws ReflectionException {
//        try {
//
            try {
                return Class.forName(element.getClassName()).getDeclaredMethod(element.getMethodName(), classArrayList.toArray(Class[]::new));
            }catch (NoSuchMethodException | ClassNotFoundException e){
                throw new ReflectionException("Can't find method with name '"+element.getMethodName()+"'");
            }

//            ArrayList<Method> machingMethods = new ArrayList<>();

//            for (Method method : Class.forName(element.getClassName()).getDeclaredMethods()) {
//
//                if(method.getName().equals(element.getMethodName()))
//                {
//                    machingMethods.add(method);
//                }
//            }
//            if(machingMethods.size() > 1){
//                if(classArrayList.size() == 0){
//                    throw new ReflectionException("Can't match method signature without parameters when same names are used for differend method. provide method params or param type classes in reflection method");
//                }
//                for (Method machingMethod : machingMethods) {
//
//                    if (machingMethod.getParameters().length != classArrayList.size()) {
//                        continue;
//                    } else {
//                        for (Parameter parameter : machingMethod.getParameters()) {
//                            if(!classArrayList.contains(parameter.getType())){
//                                break;
//                            }
//                        }
//                        return machingMethod;
//                    }
//                }
//            }else if(machingMethods.size() == 1){
//                return machingMethods.get(0);
//            }

//        } catch (ReflectionException.ShadowException | Exception e) {
//            throw new ReflectionException(e);
//        }
    }

    private static Class<?> CLASS(StackTraceElement element) throws ReflectionException {
        try {
            return Class.forName(element.getClassName());
        } catch (ClassNotFoundException e) {
            throw new ReflectionException(e);
        }
    }

}
