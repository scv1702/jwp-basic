package core.web.method;

import core.web.handler.Handler;
import core.web.Model;
import core.web.annotations.RequestHeader;
import core.web.annotations.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class HandlerArgumentResolver {

    private static final Map<Class<?>, ArgumentResolver> resolvers = new HashMap<>();

    static {
        resolvers.put(RequestParam.class, new RequestParamArgumentResolver());
        resolvers.put(RequestHeader.class, new RequestHeaderArgumentResolver());
    }

    public Object[] resolveArguments(
        Handler handler,
        Model model,
        HttpServletRequest req,
        HttpServletResponse res
    ) {
        Method method = handler.getMethod();
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            args[i] = resolve(parameters[i], model, req, res);
        }
        return args;
    }

    private Object resolve(Parameter parameter, Model model, HttpServletRequest req, HttpServletResponse res) {
        if (parameter.getType().equals(Model.class)) {
            return model;
        }
        if (parameter.getType().equals(HttpSession.class)) {
            return req.getSession();
        }
        if (parameter.getType().equals(HttpServletRequest.class)) {
            return req;
        }
        if (parameter.getType().equals(HttpServletResponse.class)) {
            return res;
        }

        for (Annotation annotation : parameter.getAnnotations()) {
            if (resolvers.containsKey(annotation.annotationType())) {
                return resolvers.get(annotation.annotationType()).resolve(parameter, req);
            }
        }

        return null;
    }
}
