package core.web;

public class ResponseEntity<T> {
    private final int status;
    private final T body;

    public ResponseEntity(int status, T body) {
        this.status = status;
        this.body = body;
    }

    public static class ResponseEntityBuilder {
        private int status;

        public ResponseEntityBuilder status(int status) {
            this.status = status;
            return this;
        }

        public <T> ResponseEntity<T> body(T body) {
            return new ResponseEntity<>(status, body);
        }
    }

    public static ResponseEntityBuilder ok() {
        return new ResponseEntityBuilder().status(200);
    }

    public static ResponseEntityBuilder created() {
        return new ResponseEntityBuilder().status(201);
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

    public T getBody() {
        return body;
    }
}
