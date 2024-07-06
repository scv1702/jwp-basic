package core.bean;

import core.bean.annotations.Inject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

public class SetterInjector implements Injector {

    private final BeanFactory beanFactory;

    public SetterInjector(final BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void inject(final Object bean) {
        Class<?> beanType = bean.getClass();
        getInjectedSetters(beanType)
            .forEach(method -> injectSetter(method, bean, beanFactory.getBeans(method.getParameterTypes())));
    }

    private void injectSetter(final Method method, final Object bean, final Object... value) {
        try {
            method.invoke(bean, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Setter injection failed for " + method.getName(), e);
        }
    }

    private Stream<Method> getInjectedSetters(final Class<?> clazz) {
        return Arrays.stream(clazz.getMethods())
            .filter(field -> field.isAnnotationPresent(Inject.class));
    }
}
