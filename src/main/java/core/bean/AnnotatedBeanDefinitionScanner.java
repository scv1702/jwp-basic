package core.bean;

import core.bean.annotations.Bean;
import core.bean.annotations.Configuration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AnnotatedBeanDefinitionScanner extends AbstractBeanDefinitionScanner {

    public AnnotatedBeanDefinitionScanner(String basePackage, BeanDefinitionRegistry beanDefinitionRegistry) {
        super(basePackage, beanDefinitionRegistry);
    }

    @Override
    protected Set<BeanDefinition> scanBeanDefinition() {
        final Set<Class<?>> configurations = getTypesAnnotatedWith(Configuration.class);
        final Set<BeanDefinition> beanDefinitions = new HashSet<>();
        for (Class<?> configuration : configurations) {
            final Object config = createConfigurationInstance(configuration);
            Arrays.stream(configuration.getMethods())
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .forEach(method -> beanDefinitions.add(new BeanDefinition(method, config)));
        }
        return beanDefinitions;
    }

    private Object createConfigurationInstance(Class<?> configuration) {
        try {
            return BeanUtils.createInstance(configuration.getDeclaredConstructor());
        } catch (Exception e) {
            throw new IllegalStateException(
                String.format("Configuration class %s has must default constructor.", configuration.getName()),
                e
            );
        }
    }
}
