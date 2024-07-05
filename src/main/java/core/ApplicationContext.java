package core;

import core.bean.BeanFactory;
import core.bean.BeanScanner;

import java.util.Set;

public class ApplicationContext {

    private final BeanScanner beanScanner;

    private final BeanFactory beanFactory;

    public ApplicationContext(String basePackage) {
        this.beanScanner = new BeanScanner(basePackage);
        this.beanFactory = new BeanFactory(beanScanner);
        this.beanFactory.initialize();
    }

    public <T> T getBean(final Class<T> beanType) {
        return beanFactory.getBean(beanType);
    }

    public Set<Object> getBeans() {
        return beanFactory.getBeans();
    }
}
