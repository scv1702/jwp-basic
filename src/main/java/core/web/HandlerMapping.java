package core.web;

import core.web.handler.Handler;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    Handler getHandler(HttpServletRequest req);
}
