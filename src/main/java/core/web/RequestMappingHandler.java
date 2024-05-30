package core.web;

import core.http.ResponseEntity;
import core.web.annotations.ResponseBody;
import core.web.view.View;
import core.web.view.DefaultView;
import core.web.view.JsonView;
import core.web.view.JspView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class RequestMappingHandler {

    private final Object controller;
    private final Method method;

    public RequestMappingHandler(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public View handle(Object[] args, Model model, HttpServletRequest req, HttpServletResponse res)
        throws InvocationTargetException, IllegalAccessException {
        Object result = method.invoke(controller, args);
        if (hasResponseBody()) {
            if (result instanceof ResponseEntity) {
                ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
                res.setStatus(responseEntity.getStatus());
                return new JsonView(responseEntity.getBody(), res);
            }
            return new DefaultView(result, res);
        }
        if (result instanceof String) {
            return new JspView((String) result, model, req, res);
        }
        return (View) result;
    }

    public Method getMethod() {
        return method;
    }

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
