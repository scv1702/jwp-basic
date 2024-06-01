package core.web.method;

import core.web.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Parameter;

public interface HandlerMethodArgumentResolver {

    boolean supports(Parameter parameter);

    Object resolve(Parameter parameter, Model model, HttpServletRequest req, HttpServletResponse res);
}
