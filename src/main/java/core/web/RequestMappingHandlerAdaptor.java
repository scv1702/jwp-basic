package core.web;

import core.web.handler.Handler;
import core.web.handler.RequestMappingHandler;
import core.web.method.HandlerMethodReturnValueHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestMappingHandlerAdaptor extends AbstractHandlerAdaptor {

    @Override
    public boolean supportsInternal(Handler handler) {
        return handler instanceof RequestMappingHandler;
    }

    @Override
    public ModelAndView handleInternal(HttpServletRequest req, HttpServletResponse res, Handler handler) throws Exception {
        Model model = new Model();
        Object[] args = resolveArguments(handler, model, req, res);

        Object result = handler.handle(args);

        HandlerMethodReturnValueHandler returnValueHandler = getHandlerMethodReturnValueHandler(result);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setModel(model);
        returnValueHandler.handleReturnValue(result, modelAndView, req, res);

        return modelAndView;
    }
}
