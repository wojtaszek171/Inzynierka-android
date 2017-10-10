package pl.pollub.android.shoppinglist.model.converter;

import android.arch.persistence.room.TypeConverter;

import pl.pollub.android.shoppinglist.util.Status;

/**
 * @author Adrian
 * @since 2017-10-08
 */
public class StatusConverter {
    @TypeConverter
    public static Status toStatus(String value) {
        return value == null ? Status.DEFAULT_STATUS : Status.valueOf(value.toUpperCase());
    }

    @TypeConverter
    public static String toString(Status status) {
        return status == null ? null : status.name();
    }
}
