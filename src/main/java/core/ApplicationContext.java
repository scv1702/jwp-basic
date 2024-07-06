package core;

import core.bean.AnnotatedBeanDefinitionScanner;
import core.bean.BeanFactory;
import core.bean.BeanDefinitionScanner;
import core.bean.ClasspathBeanDefinitionScanner;

import java.util.List;
import java.util.Set;

public class ApplicationContext {

    private final BeanFactory beanFactory;

    private final List<BeanDefinitionScanner> scanners;

    public ApplicationContext(String basePackage) {
        this.beanFactory = new BeanFactory();
        this.scanners = List.of(
            new AnnotatedBeanDefinitionScanner(basePackage, beanFactory),
            new ClasspathBeanDefinitionScanner(basePackage, beanFactory)
        );
        initialize();
    }

    private void initialize() {
        scanners.forEach(BeanDefinitionScanner::scan);
        beanFactory.initialize();
    }

    public <T> T getBean(final Class<T> beanType) {
        return beanFactory.getBean(beanType);
    }

    public Set<Object> getBeans() {
        return beanFactory.getBeans();
    }
}
