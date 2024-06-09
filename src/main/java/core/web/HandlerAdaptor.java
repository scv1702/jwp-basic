package core.web;

import core.web.handler.Handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerAdaptor {
    boolean supports(Handler handler);

    ModelAndView handle(HttpServletRequest req, HttpServletResponse resp, Handler handler) throws Exception;
}
