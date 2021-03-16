import Reflection.Reflection;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class Test {

    private ArrayList<String> name;


    public void login() throws Reflection.ReflectionException {
        System.out.println(Reflection.Method());
    }

    public void login(String method) throws Reflection.ReflectionException {
        System.out.println(Reflection.Method().toGenericString());
    }

    public void signature() {
        Reflection.
    }
}

class Ref{
    public static Method Method(){
        return new Object(){}.getClass().getEnclosingMethod();
    }
}
