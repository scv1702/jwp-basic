package core.web;

import core.http.HttpMethod;
import core.web.method.HandlerArgumentResolver;
import core.web.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private RequestMappingHandlerMapping handlerMapping;
    private final HandlerArgumentResolver handlerArgumentResolver = new HandlerArgumentResolver();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = config.getServletContext();
        handlerMapping = (RequestMappingHandlerMapping) servletContext.getAttribute("handlerMapping");
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws RuntimeException {
        try {
            HttpMethod httpMethod = HttpMethod.valueOf(req.getMethod());
            String requestURI = req.getRequestURI();
            String contextPath = req.getContextPath();
            String mappingURI = requestURI.substring(contextPath.length());

            RequestMappingHandler handler = handlerMapping.getHandler(mappingURI, httpMethod);
            Model model = new Model();
            Object[] args = handlerArgumentResolver.resolveArguments(handler, model, req, res);

            View view = handler.handle(args, model, req, res);
            view.render();
        } catch (Exception e) {
            logger.error("Failed to service", e);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
