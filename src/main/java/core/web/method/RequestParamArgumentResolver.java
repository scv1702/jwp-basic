package core.web.method;

import core.web.annotations.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;
import java.util.Map;

public class RequestParamArgumentResolver implements ArgumentResolver {

    public Object resolve(Parameter parameter, HttpServletRequest req) {
        RequestParam requestParam = parameter.getAnnotation(RequestParam.class);

        String value = req.getParameter(requestParam.value());
        boolean required = requestParam.required();

        if (value == null) {
            if (required) {
                throw new IllegalArgumentException("Required parameter is missing");
            }
            return null;
        }

        if (parameter.getType().equals(Integer.class) || parameter.getType().equals(int.class)) {
            return Integer.parseInt(value);
        }
        if (parameter.getType().equals(Long.class) || parameter.getType().equals(long.class)) {
            return Long.parseLong(value);
        }
        if (parameter.getType().equals(Double.class) || parameter.getType().equals(double.class)) {
            return Double.parseDouble(value);
        }
        if (parameter.getType().equals(Float.class) || parameter.getType().equals(float.class)) {
            return Float.parseFloat(value);
        }
        if (parameter.getType().equals(Boolean.class) || parameter.getType().equals(boolean.class)) {
            return Boolean.parseBoolean(value);
        }
        if (parameter.getType().equals(Map.class)) {
            return req.getParameterMap();
        }

        return value;
    }
}
