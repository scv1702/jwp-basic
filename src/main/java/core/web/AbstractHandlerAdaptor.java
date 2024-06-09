package core.web;

import core.web.handler.Handler;
import core.web.method.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractHandlerAdaptor implements HandlerAdaptor {

    private static final List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();
    private static final List<HandlerMethodReturnValueHandler> returnValueHandlers = new ArrayList<>();

    static {
        resolvers.add(new RequestParamMethodArgumentResolver());
        resolvers.add(new RequestHeaderMethodArgumentResolver());
        resolvers.add(new HttpServletRequestMethodArgumentResolver());
        resolvers.add(new HttpServletResponseMethodArgumentResolver());
        resolvers.add(new ModelMethodArgumentResolver());
        resolvers.add(new HttpSessionMethodArgumentResolver());

        returnValueHandlers.add(new ModelAndViewMethodReturnValueHandler());
        returnValueHandlers.add(new ViewNameMethodReturnValueHandler());
        returnValueHandlers.add(new ResponseEntityMethodReturnValueHandler());
        returnValueHandlers.add(new ViewMethodReturnValueHandler());
    }

    protected static HandlerMethodArgumentResolver getHandlerMethodArgumentResolver(Parameter parameter) {
        return resolvers.stream()
            .filter(resolver -> resolver.supports(parameter))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Unsupported parameter type"));
    }

    protected static HandlerMethodReturnValueHandler getHandlerMethodReturnValueHandler(Object result) {
        return returnValueHandlers.stream()
            .filter(handler -> handler.supportsReturnType(result.getClass()))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Unsupported return type"));
        //TODO: ResponseBodyMethodReturnValueHandler 구현
    }

    protected static Object[] resolveArguments(
        Handler handler,
        Model model,
        HttpServletRequest req,
        HttpServletResponse res
    ) {
        Method method = handler.getMethod();
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            HandlerMethodArgumentResolver resolver = getHandlerMethodArgumentResolver(parameters[i]);
            args[i] = resolver.resolve(parameters[i], model, req, res);
        }
        return args;
    }

    @Override
    public boolean supports(Handler handler) {
        return supportsInternal(handler);
    }

    @Override
    public ModelAndView handle(HttpServletRequest req, HttpServletResponse res, Handler handler) throws Exception {
        return handleInternal(req, res, handler);
    }


    protected abstract boolean supportsInternal(Handler handler);

    protected abstract ModelAndView handleInternal(HttpServletRequest req, HttpServletResponse res, Handler handler) throws Exception;
}
