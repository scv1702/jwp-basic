package core.web.handler;

import core.web.Model;

import java.lang.reflect.Method;

public interface Handler {

    Object handle(Object[] args, Model model) throws Exception;

    Method getMethod();

    boolean hasResponseBody();
}
