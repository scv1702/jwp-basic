package core.bean;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

import static core.bean.BeanFactoryUtils.findConcreteClass;
import static core.bean.BeanFactoryUtils.getInjectedConstructor;
import static core.bean.BeanUtils.createInstance;

@Slf4j
public class BeanFactory {

    private final Set<Class<?>> beanTypes = new HashSet<>();

    private final Map<Class<?>, Object> beans = new HashMap<>();

    private final BeanScanner beanScanner;

    private final List<Injector> injectors;

    public BeanFactory(BeanScanner beanScanner) {
        this.beanScanner = beanScanner;
        this.injectors = Arrays.asList(
            new FieldInjector(this),
            new SetterInjector(this)
        );
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> beanType) {
        return (T) beans.get(beanType);
    }

    public Set<Object> getBeans() {
        return new HashSet<>(beans.values());
    }

    public void initialize() {
        //TODO: 현재 @Configuration으로 Bean 등록 시 의존 관계 설정이 불가능
        beanScanner.scanBeans().forEach(bean -> beans.put(bean.getClass(), bean));
        beanTypes.addAll(beanScanner.scanComponents());
        beanTypes.stream()
            .filter(beanType -> !beanType.isAnnotation())
            .forEach(this::initializeBean);
        String beanNames = beans.keySet().stream()
            .map(Class::getName)
            .collect(Collectors.joining(", "));
        log.info("Initialized {} Beans : {}", beans.size(), beanNames);
    }

    Object initializeBean(final Class<?> beanType) {
        if (beans.containsKey(beanType)) {
            return beans.get(beanType);
        }
        Object bean = createBean(beanType);
        injectors.forEach(injector -> injector.inject(bean));
        beans.put(beanType, bean);
        return bean;
    }

    Object[] getInjectedBeans(final Class<?>[] beanTypes) {
        return Arrays.stream(beanTypes)
            .map(beanType -> findConcreteClass(beanType, this.beanTypes))
            .map(this::initializeBean)
            .toArray();
    }

    private Object createBean(Class<?> beanType) {
        final Constructor<?> constructor = getInjectedConstructor(findConcreteClass(beanType, beanTypes));
        return createInstance(constructor, getInjectedBeans(constructor.getParameterTypes()));
    }
}
