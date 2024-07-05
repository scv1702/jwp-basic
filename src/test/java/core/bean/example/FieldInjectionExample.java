package core.bean.example;

import core.bean.annotations.Component;
import core.bean.annotations.Inject;
import lombok.Getter;

@Component
public class FieldInjectionExample {

    @Getter
    @Inject
    private BaseInjectionExample baseInjectionExample;
}
