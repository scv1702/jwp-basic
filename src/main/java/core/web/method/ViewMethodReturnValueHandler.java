package core.web.method;

import core.web.ModelAndView;
import core.web.view.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ViewMethodReturnValueHandler implements HandlerMethodReturnValueHandler {
    @Override
    public boolean supportsReturnType(final Class<?> returnType) {
        return returnType.equals(View.class);
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

        modelAndView.setView((View) returnValue);
    }
}
