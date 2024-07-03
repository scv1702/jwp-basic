package core.web;

import core.bean.BeanFactory;
import core.bean.annotations.Controller;
import core.http.HttpMethod;
import core.web.annotations.RequestMapping;
import core.web.exception.NoRequestMappingHandlerException;
import core.web.handler.Handler;
import core.web.handler.RequestMappingHandler;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RequestMappingHandlerMapping implements HandlerMapping {

    private final Map<RequestMappingKey, RequestMappingHandler> requestMappingHandlers = new HashMap<>();

    public RequestMappingHandlerMapping(BeanFactory beanFactory) {
        Set<Object> controllers = beanFactory.getBeansByAnnotation(Controller.class);
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

    private static String getMappingURI(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        String contextPath = req.getContextPath();
        return requestURI.substring(contextPath.length());
    }

    public Handler getHandler(HttpServletRequest req) {
        HttpMethod method = HttpMethod.valueOf(req.getMethod());
        String mappingURI = getMappingURI(req);
        Handler handler = requestMappingHandlers.get(new RequestMappingKey(mappingURI, method));
        if (handler == null) {
            throw new NoRequestMappingHandlerException(mappingURI, method);
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
