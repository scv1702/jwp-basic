package core.bean;

import core.bean.annotations.Bean;
import core.bean.annotations.Component;
import core.bean.annotations.Configuration;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BeanScanner {

    private final Reflections reflections;

    public BeanScanner() {
        this.reflections = new Reflections();
    }

    public Set<Object> scanBeans() {
        Set<Object> beans = new HashSet<>();
        Set<Class<?>> configurations = reflections.getTypesAnnotatedWith(Configuration.class);
        for (Class<?> configuration : configurations) {
            Object config;
            try {
                config = BeanUtils.createInstance(configuration.getDeclaredConstructor());
            } catch (Exception e) {
                throw new IllegalStateException(
                    String.format("Configuration class %s has must default constructor.", configuration.getName()),
                    e
                );
            }
            Arrays.stream(configuration.getMethods())
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .forEach(method -> beans.add(BeanUtils.createInstance(method, config)));
        }
        return beans;
    }

    public Set<Class<?>> scanComponents() {
        return reflections.getTypesAnnotatedWith(Component.class);
    }
}
