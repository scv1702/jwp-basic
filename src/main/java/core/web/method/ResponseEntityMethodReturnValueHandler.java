package core.web.method;

import core.http.ResponseEntity;
import core.util.json.JsonConverter;
import core.web.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseEntityMethodReturnValueHandler implements HandlerMethodReturnValueHandler {
    private final JsonConverter jsonConverter = new JsonConverter();

    @Override
    public boolean supportsReturnType(final Class<?> returnType) {
        return returnType.equals(ResponseEntity.class);
    }

    @Override
    public void handleReturnValue(
        final Object returnValue,
        final ModelAndView modelAndView,
        final HttpServletRequest req,
        final HttpServletResponse res
    ) throws IOException {
        if (!supportsReturnType(returnValue.getClass())) {
            throw new IllegalArgumentException("Unsupported return type: " + returnValue.getClass());
        }

        ResponseEntity<?> responseEntity = (ResponseEntity<?>) returnValue;
        res.setStatus(responseEntity.getStatus());
        responseEntity.getHeaders().forEach(res::setHeader);
        res.setContentType("application/json");
        res.getWriter().print(jsonConverter.convertToJson(responseEntity.getBody()));
    }
}
