package core.web;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private RequestMappingHandlerMapping handlerMapping;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        handlerMapping = new RequestMappingHandlerMapping(ComponentScanner.scan());
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) {
        try {
            RequestMethod requestMethod = RequestMethod.valueOf(req.getMethod());
            String requestURI = req.getRequestURI();
            String contextPath = req.getContextPath();
            String mappingURI = requestURI.substring(contextPath.length());

            RequestMappingHandler handler = handlerMapping.getHandler(mappingURI, requestMethod);

            String viewName = handler.handle(req, res);

            if (viewName.startsWith("redirect:")) {
                res.sendRedirect(viewName.substring("redirect:".length()));
            } else {
                req.getRequestDispatcher(viewName + ".jsp").forward(req, res);
            }
        } catch (Exception e) {
            logger.error("Failed to service", e);
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
