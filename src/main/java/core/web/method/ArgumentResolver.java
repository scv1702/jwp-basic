package core.web.method;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;

public interface ArgumentResolver {
    Object resolve(Parameter parameter, HttpServletRequest req);
}
