package core.jdbc;

public interface PropertyConverter<T> {
    String toString(T value);
    T fromString(String value);
}
