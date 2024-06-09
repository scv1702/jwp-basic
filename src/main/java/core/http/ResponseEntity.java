package core.http;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class ResponseEntity<T> {
    private final int status;
    private final Map<String, String> headers;
    private final T body;

    public ResponseEntity(int status, Map<String, String> headers, T body) {
        this.status = status;
        this.headers = headers;
        this.body = body;
    }

    public static class ResponseEntityBuilder {
        private int status;
        private final Map<String, String> headers = new HashMap<>();

        public ResponseEntityBuilder status(int status) {
            this.status = status;
            return this;
        }

        public ResponseEntityBuilder header(String name, String value) {
            headers.put(name, value);
            return this;
        }

        public <T> ResponseEntity<T> body(T body) {
            return new ResponseEntity<>(status, headers, body);
        }

        public <T> ResponseEntity<T> build() {
            return new ResponseEntity<>(status, headers, null);
        }
    }

    public static ResponseEntityBuilder ok() {
        return new ResponseEntityBuilder().status(200);
    }

    public static ResponseEntityBuilder created(URI location) {
        return new ResponseEntityBuilder().status(201).header("Location", location.toString());
    }

    public static ResponseEntityBuilder noContent() {
        return new ResponseEntityBuilder().status(204);
    }

    public static ResponseEntityBuilder badRequest() {
        return new ResponseEntityBuilder().status(400);
    }

    public static ResponseEntityBuilder unauthorized() {
        return new ResponseEntityBuilder().status(401);
    }

    public static ResponseEntityBuilder forbidden() {
        return new ResponseEntityBuilder().status(403);
    }

    public static ResponseEntityBuilder notFound() {
        return new ResponseEntityBuilder().status(404);
    }

    public int getStatus() {
        return status;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public T getBody() {
        return body;
    }
}
