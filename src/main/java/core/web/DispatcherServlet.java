package core.web;

import core.util.json.JsonConverter;
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
    private JsonConverter jsonConverter = new JsonConverter();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = config.getServletContext();
        handlerMapping = (RequestMappingHandlerMapping) servletContext.getAttribute("handlerMapping");
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws RuntimeException {
        try {
            RequestMethod requestMethod = RequestMethod.valueOf(req.getMethod());
            String requestURI = req.getRequestURI();
            String contextPath = req.getContextPath();
            String mappingURI = requestURI.substring(contextPath.length());

            RequestMappingHandler handler = handlerMapping.getHandler(mappingURI, requestMethod);

            Object result = handler.handle(req, res);

            if (result instanceof ResponseEntity) {
                ResponseEntity responseEntity = (ResponseEntity) result;
                int status = responseEntity.getStatus();
                Object body = responseEntity.getBody();
                res.setStatus(status);
                res.addHeader("Content-Type", "application/json");
                res.getWriter().write(jsonConverter.convertToJson(body));
            } else if (handler.hasResponseBody()) {
                res.addHeader("Content-Type", "application/json");
                res.getWriter().write(jsonConverter.convertToJson(result));
            } else if (result instanceof String) {
                String viewName = (String) result;
                if (viewName.startsWith("redirect:")) {
                    res.sendRedirect(viewName.substring("redirect:".length()));
                } else {
                    req.getRequestDispatcher(viewName + ".jsp").forward(req, res);
                }
            }
        } catch (Exception e) {
            logger.error("Failed to service", e);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
