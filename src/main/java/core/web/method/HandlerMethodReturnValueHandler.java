package core.web.method;

import core.web.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerMethodReturnValueHandler {

    boolean supportsReturnType(Class<?> returnType);

    void handleReturnValue(
        Object returnValue,
        ModelAndView modelAndView,
        HttpServletRequest req,
        HttpServletResponse res
    ) throws Exception;
}
