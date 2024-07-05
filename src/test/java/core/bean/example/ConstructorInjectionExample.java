package core.bean.example;

import core.bean.annotations.Component;
import core.bean.annotations.Inject;
import lombok.Getter;

@Component
public class ConstructorInjectionExample {

    @Getter
    private final BaseInjectionExample baseInjectionExample;

    @Inject
    public ConstructorInjectionExample(BaseInjectionExample baseInjectionExample) {
        this.baseInjectionExample = baseInjectionExample;
    }
}
