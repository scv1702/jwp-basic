package core.web.handler;

import java.lang.reflect.Method;

public interface Handler {

    Object handle(Object[] args) throws Exception;

    Method getMethod();
}
