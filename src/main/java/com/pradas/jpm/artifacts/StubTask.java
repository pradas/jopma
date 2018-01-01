package main.java.com.pradas.jpm.artifacts;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class StubTask extends NodeImpl {

    protected Object obj;
    protected Method method;

    public StubTask(Method method, Object obj) {
        super(method.getName());
        this.method = method;
        this.obj = obj;
    }

    public StubTask(Method method, Object[] arguments, Object obj) {
        super(method.getName(), arguments);
        this.method = method;
        this.obj = obj;
    }

    @Override
    public void execute() {
        try {
            result = method.invoke(obj, arguments);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
