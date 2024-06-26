package core.jdbc.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

public class LocalDateTimeConverter implements PropertyConverter<LocalDateTime> {

    private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
        .appendPattern("yyyy-MM-dd HH:mm:ss")
        .optionalStart()
        .appendFraction(ChronoField.MILLI_OF_SECOND, 1, 9, true)
        .optionalEnd()
        .toFormatter();

    @Override
    public String toString(final LocalDateTime value) {
        return value.truncatedTo(ChronoUnit.SECONDS).format(FORMATTER);
    }

    @Override
    public LocalDateTime fromString(final String value) {
        return LocalDateTime.parse(value, FORMATTER).truncatedTo(ChronoUnit.SECONDS);
    }
}