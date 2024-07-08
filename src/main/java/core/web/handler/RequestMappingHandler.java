package core.web.handler;

import lombok.EqualsAndHashCode;

import java.lang.reflect.Method;

@EqualsAndHashCode
public class RequestMappingHandler implements Handler {

    private final Object controller;
    private final Method method;

    public RequestMappingHandler(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    @Override
    public Object handle(Object[] args) throws Exception {
        return method.invoke(controller, args);
    }

    @Override
    public Method getMethod() {
        return method;
    }
}
