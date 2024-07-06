package core.bean;

import core.bean.annotations.Inject;
import lombok.Getter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Set;

import static org.reflections.ReflectionUtils.getAllConstructors;
import static org.reflections.util.ReflectionUtilsPredicates.withAnnotation;

public class BeanDefinition {

    static class BeanConstructor {
        private final Constructor<?> constructor;
        private final Method method;
        private final Object object;

        private BeanConstructor(Constructor<?> constructor) {
            this.constructor = constructor;
            this.method = null;
            this.object = null;
        }

        private BeanConstructor(Method method, Object object) {
            this.constructor = null;
            this.method = method;
            this.object = object;
        }

        private Object createInstance(Object... parameters) {
            if (constructor != null) {
                return BeanUtils.createInstance(constructor, parameters);
            }
            return BeanUtils.createInstance(method, object, parameters);
        }

        private Class<?>[] getParameterTypes() {
            if (constructor != null) {
                return constructor.getParameterTypes();
            }
            assert method != null;
            return method.getParameterTypes();
        }
    }

    @Getter
    private final Class<?> beanType;

    private final BeanConstructor beanConstructor;

    public BeanDefinition(Class<?> beanType) {
        this.beanType = beanType;
        this.beanConstructor = new BeanConstructor(getInjectedConstructor(beanType));
    }

    public BeanDefinition(Method method, Object object) {
        this.beanType = method.getReturnType();
        this.beanConstructor = new BeanConstructor(method, object);
    }

    public Object createBean(Object... parameters) {
        return beanConstructor.createInstance(parameters);
    }

    public Class<?>[] getInjectedParameterTypes() {
        return beanConstructor.getParameterTypes();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Constructor<?> getInjectedConstructor(Class<?> clazz) {
        Set<Constructor> injectedConstructors = getAllConstructors(clazz, withAnnotation(Inject.class));
        if (injectedConstructors.isEmpty()) {
            try {
                return clazz.getDeclaredConstructor();
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException("No default constructor found for " + clazz.getName());
            }
        }
        return injectedConstructors.iterator().next();
    }
}
