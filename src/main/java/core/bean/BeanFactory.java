package core.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static core.bean.BeanFactoryUtils.findConcreteClass;
import static core.bean.BeanFactoryUtils.getInjectedConstructor;

public class BeanFactory {

    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private final Set<Class<?>> beanTypes;

    private final Map<Class<?>, Object> beans = new HashMap<>();

    public BeanFactory(Set<Class<?>> beanTypes) {
        this.beanTypes = beanTypes;
        initialize();
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) {
        return (T) beans.get(clazz);
    }

    public Set<Object> getBeansByAnnotation(Class<? extends Annotation> annotation) {
        return beans.keySet().stream()
            .filter(clazz -> clazz.isAnnotationPresent(annotation))
            .map(beans::get)
            .collect(Collectors.toSet());
    }

    private void initialize() {
        for (Class<?> beanType : beanTypes) {
            if (beanType.isAnnotation()) {
                continue;
            }
            initializeBean(beanType);
        }
        String beanNames = beans.keySet().stream()
            .map(Class::getName)
            .reduce((a, b) -> a + ", " + b)
            .orElse("");
        logger.info("Initialized Beans : {}", beanNames);
    }

    private Object initializeBean(Class<?> beanType) {
        if (beans.containsKey(beanType)) {
            return beans.get(beanType);
        }
        Object bean = createBean(getInjectedConstructor(beanType));
        beans.put(beanType, bean);
        return bean;
    }

    private Object createBean(Constructor<?> constructor) {
        try {
            return constructor.newInstance(resolveParameters(constructor));
        } catch (Exception exception) {
            throw new IllegalStateException("Bean initialization failed for " + constructor.getName(), exception);
        }
    }

    private Object[] resolveParameters(Constructor<?> constructor) {
        return Arrays.stream(constructor.getParameterTypes())
            .map(parameterType -> findConcreteClass(parameterType, beanTypes))
            .map(this::initializeBean)
            .toArray();
    }
}
