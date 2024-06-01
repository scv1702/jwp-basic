package core.web.method;

import core.web.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ModelAndViewMethodReturnValueHandler implements HandlerMethodReturnValueHandler {
    @Override
    public boolean supportsReturnType(final Class<?> returnType) {
        return returnType.equals(ModelAndView.class);
    }

    @Override
    public void handleReturnValue(
        final Object returnValue,
        final ModelAndView modelAndView,
        final HttpServletRequest req,
        final HttpServletResponse res
    ) {
        if (!supportsReturnType(returnValue.getClass())) {
            throw new IllegalArgumentException("Unsupported return type: " + returnValue.getClass());
        }

        ModelAndView returned = (ModelAndView) returnValue;
        if (returned.getViewName() != null) {
            modelAndView.setViewName(returned.getViewName());
        }
        if (returned.getView() != null) {
            modelAndView.setView(returned.getView());
        }
        modelAndView.setModel(returned.getModel());
    }
}
