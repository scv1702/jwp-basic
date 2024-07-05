package core.bean;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class BeanScannerTest {

    private final BeanScanner beanScanner = new BeanScanner("core.bean.example");

    @Test
    void scanComponents() {
        for (Object bean : beanScanner.scanComponents()) {
            log.debug("Bean : {}", bean);
        }
    }
}