package core.context;

import core.context.annotations.Component;
import org.reflections.Reflections;

import java.util.Set;

public class BeanScanner {

    private final Reflections reflections;

    public BeanScanner() {
        reflections = new Reflections();
    }

    public BeanScanner(String basePackage) {
        reflections = new Reflections(basePackage);
    }

    public Set<Class<?>> scan() {
        return reflections.getTypesAnnotatedWith(Component.class);
    }
}
