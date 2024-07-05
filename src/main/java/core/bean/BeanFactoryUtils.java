package core.bean;

import core.bean.annotations.Inject;
import core.bean.annotations.Primary;

import java.lang.reflect.Constructor;
import java.util.Set;
import java.util.stream.Collectors;

import static org.reflections.ReflectionUtils.getAllConstructors;
import static org.reflections.ReflectionUtils.withAnnotation;

public class BeanFactoryUtils {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Constructor<?> getInjectedConstructor(Class<?> clazz) {
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

    public static Class<?> findConcreteClass(Class<?> clazz, Set<Class<?>> beanTypes) {
        if (!clazz.isInterface()) {
            return clazz;
        }

        Set<Class<?>> subTypes = beanTypes.stream()
            .filter(clazz::isAssignableFrom)
            .collect(Collectors.toSet());

        if (subTypes.isEmpty()) {
            throw new IllegalStateException("Cannot find concrete class for " + clazz.getName());
        }

        if (subTypes.size() == 1) {
            return subTypes.iterator().next();
        }

        for (Class<?> concreteClass : subTypes) {
            if (concreteClass.isAnnotationPresent(Primary.class)) {
                return concreteClass;
            }
        }

        throw new IllegalStateException("Concrete class is ambiguous for " + clazz.getName());
    }
}