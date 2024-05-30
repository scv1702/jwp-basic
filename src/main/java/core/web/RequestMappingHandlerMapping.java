package core.web;

import core.http.HttpMethod;
import core.context.annotations.Controller;
import core.web.annotations.RequestMapping;
import core.web.exception.NoRequestMappingHandlerException;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    public RequestMappingHandler getHandler(String mappingURI, HttpMethod httpMethod) {
        RequestMappingHandler handler =
            requestMappingHandlers.get(new RequestMappingKey(mappingURI, httpMethod));
        if (handler == null) {
            throw new NoRequestMappingHandlerException(mappingURI, httpMethod);
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
