package core.bean;

import core.bean.annotations.*;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BeanScanner {

    private final Reflections reflections;

    public BeanScanner(String basePackage) {
        this.reflections = new Reflections(
            new ConfigurationBuilder()
                .forPackages(basePackage)
                .setScanners(Scanners.TypesAnnotated)
                .filterInputsBy(new FilterBuilder().includePackage(basePackage))
        );
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
        Set<Class<?>> components = new HashSet<>();
        components.addAll(reflections.getTypesAnnotatedWith(Component.class));
        components.addAll(reflections.getTypesAnnotatedWith(Service.class));
        components.addAll(reflections.getTypesAnnotatedWith(Controller.class));
        components.addAll(reflections.getTypesAnnotatedWith(Repository.class));
        return components;
    }
}
