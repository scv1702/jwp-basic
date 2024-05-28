package core.jdbc.converter;

public class LongConverter implements PropertyConverter<Long> {

    @Override
    public String toString(final Long value) {
        return value.toString();
    }

    @Override
    public Long fromString(final String value) {
        return Long.parseLong(value);
    }
}