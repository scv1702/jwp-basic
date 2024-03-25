package core.web;

import core.web.exception.NoRequestMappingHandlerException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class RequestMappingKey {

    private final String uri;
    private final RequestMethod method;

    public RequestMappingKey(String uri, RequestMethod method) {
        this.uri = uri;
        this.method = method;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RequestMappingKey) {
            RequestMappingKey other = (RequestMappingKey) obj;
            return uri.equals(other.uri) && method.equals(other.method);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uri, method);
    }
}

class RequestMappingHandler {

    private final Object controller;
    private final Method method;

    public RequestMappingHandler(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public String handle(HttpServletRequest req, HttpServletResponse res)
        throws InvocationTargetException, IllegalAccessException {
        return (String) method.invoke(controller, req, res);
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

public class RequestMappingHandlerMapping {

    private final Map<RequestMappingKey, RequestMappingHandler> requestMappingHandlers = new HashMap<>();

    public RequestMappingHandlerMapping(Map<String, Object> components) {
        Set<Object> controllers = components.values().stream()
            .filter(component -> component.getClass().isAnnotationPresent(Controller.class))
            .collect(Collectors.toSet());

        for (Object controller : controllers) {
            Method[] methods = controller.getClass().getDeclaredMethods();
            for (Method method : methods) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                if (requestMapping == null) {
                    continue;
                }
                String uri = getBaseRequestMappingValue(controller) + requestMapping.value();
                requestMappingHandlers.put(
                    new RequestMappingKey(uri, requestMapping.method()),
                    new RequestMappingHandler(controller, method));
            }
        }
    }

    public RequestMappingHandler getHandler(String mappingURI, RequestMethod requestMethod) {
        RequestMappingHandler handler =
            requestMappingHandlers.get(new RequestMappingKey(mappingURI, requestMethod));
        if (handler == null) {
            throw new NoRequestMappingHandlerException(mappingURI, requestMethod);
        }
        return handler;
    }

    private String getBaseRequestMappingValue(Object controller) {
        RequestMapping controllerRequestMapping = controller.getClass()
            .getAnnotation(RequestMapping.class);
        if (controllerRequestMapping == null) {
            return "";
        }
        return controllerRequestMapping.value();
    }
}
