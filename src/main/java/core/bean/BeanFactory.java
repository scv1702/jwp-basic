package core.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static core.bean.BeanFactoryUtils.*;
import static core.bean.BeanUtils.createInstance;

public class BeanFactory {

    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private final Set<Class<?>> beanTypes = new HashSet<>();

    private final Map<Class<?>, Object> beans = new HashMap<>();

    private final BeanScanner beanScanner;

    public BeanFactory(BeanScanner beanScanner) {
        this.beanScanner = beanScanner;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) {
        return (T) beans.get(clazz);
    }

    public Set<Object> getBeansByAnnotation(Class<? extends Annotation> annotation) {
        return beans.keySet().stream()
            .filter(clazz -> clazz.isAnnotationPresent(annotation))
            .map(beans::get)
            .collect(Collectors.toSet());
    }

    public void initialize() {
        //TODO: 현재 @Configuration으로 Bean 등록 시 의존 관계 설정이 불가능
        beanScanner.scanBeans().forEach(bean -> beans.put(bean.getClass(), bean));
        beanTypes.addAll(beanScanner.scanComponents());
        for (Class<?> beanType : beanTypes) {
            if (beanType.isAnnotation()) {
                continue;
            }
            initializeBean(beanType);
        }
        String beanNames = beans.keySet().stream()
            .map(Class::getName)
            .reduce((a, b) -> a + ", " + b)
            .orElse("");
        logger.info("Initialized Beans : {}", beanNames);
    }

    private Object initializeBean(Class<?> beanType) {
        if (beans.containsKey(beanType)) {
            return beans.get(beanType);
        }

        final Constructor<?> constructor = getInjectedConstructor(beanType);

        // 1. 생성자 주입
        Object bean = createInstance(constructor, resolveParameters(constructor));

        // 2. 필드 주입
        getInjectedFields(beanType)
            .forEach(field -> {
                Class<?> fieldType = field.getType();
                injectField(field, bean, initializeBean(findConcreteClass(fieldType, beanTypes)));
            });

        // 3. Setter 주입
        getInjectedSetters(beanType)
            .forEach(method -> injectSetter(method, bean, resolveParameters(method)));

        beans.put(beanType, bean);

        return bean;
    }

    private Object[] resolveParameters(Constructor<?> constructor) {
        return Arrays.stream(constructor.getParameterTypes())
            .map(parameterType -> findConcreteClass(parameterType, beanTypes))
            .map(this::initializeBean)
            .toArray();
    }

    private Object[] resolveParameters(Method method) {
        return Arrays.stream(method.getParameterTypes())
            .map(parameterType -> findConcreteClass(parameterType, beanTypes))
            .map(this::initializeBean)
            .toArray();
    }
}
