package core.context;

import core.context.annotations.Component;
import core.web.exception.ComponentScanException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComponentScanner {

    private static final Logger log = LoggerFactory.getLogger(ComponentScanner.class);
    private static final Reflections reflections = new Reflections();

    public static Map<String, Object> scan() {
        Map<String, Object> components = new HashMap<>();
        try {
            Set<Class<?>> componentClasses = reflections.getTypesAnnotatedWith(Component.class);
            for (Class<?> componentclass: componentClasses) {
                if (componentclass.isAnnotation()) {
                    continue;
                }
                Constructor<?> constructor = componentclass.getDeclaredConstructor();
                constructor.setAccessible(true);
                Object instance = constructor.newInstance();
                components.put(componentclass.getName(), instance);
            }
        } catch (Exception e) {
            throw new ComponentScanException(e.getMessage());
        }
        log.info("Scanned components: {}", components);
        return components;
    }
}
