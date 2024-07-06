package core.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class BeanUtils {

    public static Object createInstance(Constructor<?> constructor, Object... parameters) {
        try {
            return constructor.newInstance(parameters);
        } catch (Exception exception) {
            throw new IllegalStateException("Instance creation failed for " + constructor.getName(), exception);
        }
    }

    public static Object createInstance(Method method, Object object, Object... parameters) {
        try {
            return method.invoke(object, parameters);
        } catch (Exception exception) {
            throw new IllegalStateException("Instance creation failed for " + method.getReturnType().getName(), exception);
        }
    }
}
