package core.web.method;

import core.http.HttpHeaders;
import core.web.Model;
import core.web.annotations.RequestHeader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Parameter;
import java.util.Enumeration;

public class RequestHeaderMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supports(Parameter parameter) {
       return parameter.isAnnotationPresent(RequestHeader.class);
    }

    @Override
    public Object resolve(Parameter parameter, Model model, HttpServletRequest req, HttpServletResponse res) {
        if (!supports(parameter)) {
            throw new IllegalArgumentException("Unsupported parameter type");
        }

        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.addHeader(headerName, req.getHeader(headerName));
        }
        return headers;
    }
}
