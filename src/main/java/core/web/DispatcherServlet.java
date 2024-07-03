package core.web;

import core.context.BeanFactory;
import core.context.BeanScanner;
import core.web.handler.Handler;
import core.web.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMapping handlerMapping;
    private HandlerAdaptor handlerAdaptor;
    private ViewResolver viewResolver;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        BeanScanner beanScanner = new BeanScanner();
        BeanFactory beanFactory = new BeanFactory(beanScanner.scan());

        handlerMapping = new RequestMappingHandlerMapping(beanFactory);
        handlerAdaptor = new RequestMappingHandlerAdaptor();
        viewResolver = new InternalResourceViewResolver();
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws RuntimeException {
        try {
            Handler handler = handlerMapping.getHandler(req);
            ModelAndView modelAndView = handlerAdaptor.handle(req, res, handler);
            render(modelAndView, req, res);
        } catch (Exception e) {
            logger.error("Failed to service", e);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public void render(ModelAndView modelAndView, HttpServletRequest req, HttpServletResponse res) throws Exception {
        if (modelAndView == null) {
            return ;
        }

        View view = modelAndView.getView();
        if (view == null) {
            String viewName = modelAndView.getViewName();
            if (viewName != null) {
                view = viewResolver.resolveViewName(modelAndView.getViewName());
            }
        }

        if (view == null) {
            return ;
        }

        view.render(modelAndView.getModel(), req, res);
    }
}
