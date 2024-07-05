package core.bean.example;

import core.bean.annotations.Component;
import core.bean.annotations.Inject;
import lombok.Getter;

@Component
public class SetterInjectionExample {

    @Getter
    private BaseInjectionExample baseInjectionExample;

    @Inject
    public void setBaseInjectionExample(BaseInjectionExample baseInjectionExample) {
        this.baseInjectionExample = baseInjectionExample;
    }
}
