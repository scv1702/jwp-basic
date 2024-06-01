package core.web;

import core.web.handler.Handler;
import core.web.annotations.RequestMapping;
import core.web.view.JspView;
import core.web.view.View;

import java.lang.reflect.Method;

public class ViewResolver {

    private static String requestMappingToViewName(Handler handler) {
        Method method = handler.getMethod();
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String value = requestMapping.value();
        if (value.startsWith("/")) {
            return value.substring(1);
        }
        return value;
    }

    private static String getViewName(Handler handler, Object result) {
        if (result instanceof String) {
            return (String) result;
        }
        return requestMappingToViewName(handler);
    }

    public View resolveView(Handler handler, Object result) {
        if (result instanceof ModelAndView) {
            return ((ModelAndView) result).getView();
        }
        String viewName = getViewName(handler, result);
        return new JspView(viewName);
    }
}
