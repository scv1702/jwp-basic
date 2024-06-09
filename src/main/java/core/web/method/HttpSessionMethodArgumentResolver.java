package core.web.method;

import core.web.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Parameter;

public class HttpSessionMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supports(Parameter parameter) {
        return parameter.getType().equals(HttpSession.class);
    }

    @Override
    public Object resolve(Parameter parameter, Model model, HttpServletRequest req, HttpServletResponse res) {
        if (!supports(parameter)) {
            throw new IllegalArgumentException("Unsupported parameter type");
        }
        return req.getSession();
    }
}
