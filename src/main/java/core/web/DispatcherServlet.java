package core.web;

import core.http.ResponseEntity;
import core.util.json.JsonConverter;
import core.web.handler.Handler;
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
    private HandlerMapping handlerMapping;
    private final HandlerArgumentResolver handlerArgumentResolver = new HandlerArgumentResolver();
    private final ViewResolver viewResolver = new ViewResolver();
    private final JsonConverter jsonConverter = new JsonConverter();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = config.getServletContext();
        handlerMapping = (HandlerMapping) servletContext.getAttribute("handlerMapping");
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws RuntimeException {
        try {
            Handler handler = handlerMapping.getHandler(req);

            Model model = new Model();
            Object[] args = handlerArgumentResolver.resolveArguments(handler, model, req, res);

            Object result = handler.handle(args, model);

            if (handler.hasResponseBody()) {
                res.setContentType("application/json");
                res.setCharacterEncoding("UTF-8");
                Object body = result;
                if (result instanceof ResponseEntity) {
                    ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
                    res.setStatus(responseEntity.getStatus());
                    body = jsonConverter.convertToJson(responseEntity.getBody());
                }
                res.getWriter().print(body);
                return ;
            }

            View view = viewResolver.resolveView(handler, result);
            view.render(model, req, res);
        } catch (Exception e) {
            logger.error("Failed to service", e);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
