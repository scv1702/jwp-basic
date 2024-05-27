package core.web;

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

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = config.getServletContext();
        handlerMapping = (RequestMappingHandlerMapping) servletContext.getAttribute("handlerMapping");
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) {
        try {
            RequestMethod requestMethod = RequestMethod.valueOf(req.getMethod());
            String requestURI = req.getRequestURI();
            String contextPath = req.getContextPath();
            String mappingURI = requestURI.substring(contextPath.length());

            RequestMappingHandler handler = handlerMapping.getHandler(mappingURI, requestMethod);

            String result = handler.handle(req, res);

            if (handler.hasResponseBody()) {
                res.addHeader("Content-Type", "application/json");
                res.getWriter().write(result);
            } else if (result.startsWith("redirect:")) {
                res.sendRedirect(result.substring("redirect:".length()));
            } else {
                req.getRequestDispatcher(result + ".jsp").forward(req, res);
            }
        } catch (Exception e) {
            logger.error("Failed to service", e);
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
