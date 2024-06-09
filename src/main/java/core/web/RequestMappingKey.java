package core.web;

import core.http.HttpMethod;

import java.util.Objects;

public class RequestMappingKey {

    private final String uri;
    private final HttpMethod method;

    public RequestMappingKey(String uri, HttpMethod method) {
        this.uri = uri;
        this.method = method;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RequestMappingKey) {
            RequestMappingKey other = (RequestMappingKey) obj;
            return uri.equals(other.uri) && method.equals(other.method);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uri, method);
    }
}