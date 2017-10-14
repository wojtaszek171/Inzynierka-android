package pl.pollub.shoppinglist.model.converter;

import android.arch.persistence.room.TypeConverter;

import pl.pollub.shoppinglist.util.Icon;

/**
 * @author Adrian
 * @since 2017-10-08
 */
public class IconConverter {
    @TypeConverter
    public static Icon toIcon(String value) {
        return value == null ? Icon.UNKNOWN : Icon.valueOf(value.toUpperCase());
    }

    @TypeConverter
    public static String toString(Icon icon) {
        return icon == null ? null : icon.name();
    }
}
