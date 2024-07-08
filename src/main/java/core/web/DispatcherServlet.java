package core.web;

import core.ApplicationContext;
import core.web.handler.Handler;
import core.web.view.View;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class DispatcherServlet extends HttpServlet {

    private final HandlerMapping handlerMapping;
    private final HandlerAdaptor handlerAdaptor;
    private final ViewResolver viewResolver;

    public DispatcherServlet(ApplicationContext ac) {
        handlerMapping = new RequestMappingHandlerMapping(ac);
        handlerAdaptor = new RequestMappingHandlerAdaptor();
        viewResolver = new InternalResourceViewResolver();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws RuntimeException {
        try {
            final Handler handler = handlerMapping.getHandler(req);
            final ModelAndView modelAndView = handlerAdaptor.handle(req, res, handler);
            render(modelAndView, req, res);
        } catch (Exception e) {
            log.error("Failed to service", e);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public void render(ModelAndView modelAndView, HttpServletRequest req, HttpServletResponse res) throws Exception {
        if (modelAndView == null) {
            return ;
        }

        View view = resolveView(modelAndView);
        if (view == null) {
            return ;
        }

        view.render(modelAndView.getModel(), req, res);
    }

    private View resolveView(ModelAndView modelAndView) {
        if (modelAndView.getView() != null) {
            return modelAndView.getView();
        }

        String viewName = modelAndView.getViewName();
        if (viewName == null) {
            return null;
        }

        return viewResolver.resolveViewName(viewName);
    }
}
