package core.web.handler;

import core.web.annotations.ResponseBody;

import java.lang.reflect.Method;
import java.util.Objects;

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

    @Override
    public boolean hasResponseBody() {
        return controller.getClass().isAnnotationPresent(ResponseBody.class) ||
            method.isAnnotationPresent(ResponseBody.class);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RequestMappingHandler) {
            RequestMappingHandler other = (RequestMappingHandler) obj;
            return controller.equals(other.controller) && method.equals(other.method);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(controller, method);
    }
}
