package core.jdbc;

public class IntegerConverter implements PropertyConverter<Integer> {

    @Override
    public String toString(final Integer value) {
        return value.toString();
    }

    @Override
    public Integer fromString(final String value) {
        return Integer.parseInt(value);
    }
}