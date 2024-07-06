package core.bean;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.annotation.Annotation;
import java.util.Set;

public abstract class AbstractBeanScanner implements BeanScanner {

    private final Reflections reflections;

    protected final BeanDefinitionRegistry beanDefinitionRegistry;

    public AbstractBeanScanner(String basePackage, BeanDefinitionRegistry beanDefinitionRegistry) {
        this.reflections = new Reflections(
            new ConfigurationBuilder()
                .forPackages(basePackage)
                .setScanners(Scanners.TypesAnnotated)
                .filterInputsBy(new FilterBuilder().includePackage(basePackage))
        );
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }

    protected Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation) {
        return reflections.getTypesAnnotatedWith(annotation);
    }

    protected abstract Set<BeanDefinition> scanBeanDefinition();

    @Override
    public void scan() {
        scanBeanDefinition().forEach(beanDefinitionRegistry::registerBeanDefinition);
    }
}
