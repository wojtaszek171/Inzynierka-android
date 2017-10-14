package pl.pollub.shoppinglist.model.converter;

import android.arch.persistence.room.TypeConverter;

import org.threeten.bp.LocalDateTime;

/**
 * @author Adrian
 * @since 2017-10-08
 */
public class LocalDateTimeConverter {
    @TypeConverter
    public static LocalDateTime toLocalDateTime(String value) {
        return value == null ? null : LocalDateTime.parse(value);
    }

    @TypeConverter
    public static String toTimestamp(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.toString();
    }
}
