package core.bean;

import core.bean.annotations.Inject;
import core.bean.annotations.Primary;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @SuppressWarnings("unchecked")
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

    public static void injectField(Field field, Object bean, Object value) {
        try {
            field.setAccessible(true);
            field.set(bean, value);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Field injection failed for " + field.getName(), e);
        }
    }

    public static Stream<Field> getInjectedFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(Inject.class));
    }

    public static void injectSetter(Method method, Object bean, Object... value) {
        try {
            method.invoke(bean, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Stream<Method> getInjectedSetters(Class<?> clazz) {
        return Arrays.stream(clazz.getMethods())
            .filter(field -> field.isAnnotationPresent(Inject.class));
    }
}