package core.bean;

import core.bean.example.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DependencyInjectionTest {

    private final BeanScanner beanScanner = new BeanScanner("core.bean.example");
    private final BeanFactory beanFactory = new BeanFactory(beanScanner);

    @Test
    void initialize() {
        beanFactory.initialize();

        assertThat(beanFactory.getBean(BaseInjectionExample.class)).isNotNull();
        assertThat(beanFactory.getBean(ConstructorInjectionExample.class)).isNotNull();
        assertThat(beanFactory.getBean(FieldInjectionExample.class)).isNotNull();
        assertThat(beanFactory.getBean(SetterInjectionExample.class)).isNotNull();
        assertThat(beanFactory.getBean(ConstructorInjectionExample.class)).isNotNull();

        ConstructorInjectionExample ex1 = beanFactory.getBean(ConstructorInjectionExample.class);
        assertThat(ex1.getBaseInjectionExample()).isNotNull();

        FieldInjectionExample ex2 = beanFactory.getBean(FieldInjectionExample.class);
        assertThat(ex2.getBaseInjectionExample()).isNotNull();

        SetterInjectionExample ex3 = beanFactory.getBean(SetterInjectionExample.class);
        assertThat(ex3.getBaseInjectionExample()).isNotNull();

        ComplexInjectionExample ex4 = beanFactory.getBean(ComplexInjectionExample.class);
        assertThat(ex4.getConstructorInjectionExample()).isNotNull();
        assertThat(ex4.getFieldInjectionExample()).isNotNull();
        assertThat(ex4.getSetterInjectionExample()).isNotNull();
    }
}