package core.bean;

import core.bean.annotations.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ClasspathBeanScanner extends AbstractBeanScanner {

    public ClasspathBeanScanner(String basePackage, BeanDefinitionRegistry beanDefinitionRegistry) {
        super(basePackage, beanDefinitionRegistry);
    }

    @Override
    protected Set<BeanDefinition> scanBeanDefinition() {
        Set<Class<?>> components = new HashSet<>();
        components.addAll(getTypesAnnotatedWith(Component.class));
        components.addAll(getTypesAnnotatedWith(Service.class));
        components.addAll(getTypesAnnotatedWith(Controller.class));
        components.addAll(getTypesAnnotatedWith(Repository.class));
        return components.stream()
            .map(BeanDefinition::new)
            .collect(Collectors.toSet());
    }
}
