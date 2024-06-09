package next.util;

public class ApiResult {

    public static class ApiSuccessResult<T> {
        public String message;
        public T data;

        public ApiSuccessResult(String message, T data) {
            this.message = message;
            this.data = data;
        }
    }

    public static class ApiErrorResult {
        public String message;
        public String error;

        public ApiErrorResult(String message, String error) {
            this.message = message;
            this.error = error;
        }
    }

    public static ApiSuccessResult<?> success(String message) {
        return new ApiSuccessResult<>(message, null);
    }

    public static <T> ApiSuccessResult<T> success(T data) {
        return new ApiSuccessResult<>(null, data);
    }

    public static <T> ApiSuccessResult<T> success(String message, T data) {
        return new ApiSuccessResult<>(message, data);
    }

    public static ApiErrorResult error(String message, Exception e) {
        return new ApiErrorResult(message, e.getMessage());
    }

    public static ApiErrorResult error(String message) {
        return new ApiErrorResult(message, null);
    }
}
