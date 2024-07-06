package core.bean;

import core.bean.annotations.Inject;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Stream;

public class FieldInjector implements Injector {

    private final BeanFactory beanFactory;

    public FieldInjector(final BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void inject(final Object bean) {
        Class<?> beanType = bean.getClass();
        getInjectedFields(beanType)
            .forEach(field -> {
                Class<?> fieldType = field.getType();
                injectField(field, bean, beanFactory.getBean(fieldType));
            });
    }

    private void injectField(final Field field, final Object bean, final Object value) {
        try {
            field.setAccessible(true);
            field.set(bean, value);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Field injection failed for " + field.getName(), e);
        }
    }

    private Stream<Field> getInjectedFields(final Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(Inject.class));
    }
}
