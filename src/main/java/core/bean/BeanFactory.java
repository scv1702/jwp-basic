package core.bean;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class BeanFactory implements BeanDefinitionRegistry {

    private final Map<Class<?>, BeanDefinition> beanDefinitions = new HashMap<>();

    private final Map<Class<?>, Object> beans = new HashMap<>();

    private final List<Injector> injectors = List.of(
        new FieldInjector(this),
        new SetterInjector(this)
    );

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) {
        if (beans.containsKey(clazz)) {
            return (T) beans.get(clazz);
        }
        final Class<?> beanType = getConcreteClass(clazz);
        if (beanType == null) {
            return null;
        }
        return (T) initializeBean(beanDefinitions.get(beanType));
    }

    public Object[] getBeans(final Class<?>[] classes) {
        return Arrays.stream(classes)
            .map(this::getBean)
            .toArray();
    }

    public Set<Object> getBeans() {
        return new HashSet<>(beans.values());
    }

    @Override
    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        beanDefinitions.put(beanDefinition.getBeanType(), beanDefinition);
    }

    public void initialize() {
        try {
            beanDefinitions.values().forEach(this::initializeBean);
            final String beanNames = beans.keySet().stream()
                .map(Class::getName)
                .collect(Collectors.joining(", "));
            log.info("Initialized {} Beans : {}", beans.size(), beanNames);
        } catch (Exception e) {
            log.error("Failed to initialize beans", e);
        }
    }

    private Object initializeBean(final BeanDefinition beanDefinition) {
        final Class<?> beanType = beanDefinition.getBeanType();
        final Object[] injectedBeans = getBeans(beanDefinition.getInjectedParameterTypes());
        final Object bean = beanDefinition.createBean(injectedBeans);
        injectors.forEach(injector -> injector.inject(bean));
        beans.put(beanType, bean);
        return bean;
    }

    private Class<?> getConcreteClass(Class<?> clazz) {
        return beanDefinitions.keySet().stream()
            .filter(clazz::isAssignableFrom)
            .findFirst()
            .orElse(null);
    }
}
