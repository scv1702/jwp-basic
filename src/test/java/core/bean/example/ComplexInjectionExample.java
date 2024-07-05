package core.bean.example;

import core.bean.annotations.Component;
import core.bean.annotations.Inject;
import lombok.Getter;

@Component
@Getter
public class ComplexInjectionExample {

    private final ConstructorInjectionExample constructorInjectionExample;

    @Inject
    private FieldInjectionExample fieldInjectionExample;

    private SetterInjectionExample setterInjectionExample;

    @Inject
    public ComplexInjectionExample(ConstructorInjectionExample constructorInjectionExample) {
        this.constructorInjectionExample = constructorInjectionExample;
    }

    @Inject
    public void setSetterInjectionExample(SetterInjectionExample setterInjectionExample) {
        this.setterInjectionExample = setterInjectionExample;
    }
}
