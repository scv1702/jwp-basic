package core;

import core.bean.AnnotatedBeanScanner;
import core.bean.BeanFactory;
import core.bean.BeanScanner;
import core.bean.ClasspathBeanScanner;

import java.util.List;
import java.util.Set;

public class ApplicationContext {

    private final BeanFactory beanFactory;

    private final List<BeanScanner> beanScanners;

    public ApplicationContext(String basePackage) {
        this.beanFactory = new BeanFactory();
        this.beanScanners = List.of(
            new AnnotatedBeanScanner(basePackage, beanFactory),
            new ClasspathBeanScanner(basePackage, beanFactory)
        );
        initialize();
    }

    private void initialize() {
        beanScanners.forEach(BeanScanner::scan);
        beanFactory.initialize();
    }

    public <T> T getBean(final Class<T> beanType) {
        return beanFactory.getBean(beanType);
    }

    public Set<Object> getBeans() {
        return beanFactory.getBeans();
    }
}
