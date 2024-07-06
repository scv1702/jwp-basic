package core.bean.example;

import core.bean.annotations.Bean;
import core.bean.annotations.Configuration;

@Configuration
public class ConfigurationInjectionExample {

    @Bean
    public String message() {
        return "Hello, World!";
    }
}
