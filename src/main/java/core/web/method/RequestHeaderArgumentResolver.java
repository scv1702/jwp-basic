package core.web.method;

import core.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;
import java.util.Enumeration;

public class RequestHeaderArgumentResolver implements ArgumentResolver {

    public Object resolve(Parameter parameter, HttpServletRequest req) {
        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.addHeader(headerName, req.getHeader(headerName));
        }
        return headers;
    }
}
