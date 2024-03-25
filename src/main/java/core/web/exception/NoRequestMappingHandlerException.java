package core.web.exception;

import core.web.RequestMethod;

public class NoRequestMappingHandlerException extends RuntimeException {

    public NoRequestMappingHandlerException(String mappingUri, RequestMethod requestMethod) {
        super("No handler found for " + requestMethod + " " + mappingUri);
    }
}
