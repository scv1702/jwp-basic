package core.web.exception;

import core.http.HttpMethod;

public class NoRequestMappingHandlerException extends RuntimeException {

    public NoRequestMappingHandlerException(String mappingUri, HttpMethod httpMethod) {
        super("No handler found for " + httpMethod + " " + mappingUri);
    }
}
