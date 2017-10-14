package pl.pollub.shoppinglist.model.converter;

import android.arch.persistence.room.TypeConverter;

import pl.pollub.shoppinglist.util.Measure;

/**
 * @author Adrian
 * @since 2017-10-08
 */
public class MeasureConverter {
    @TypeConverter
    public static Measure toMeasure(String value) {
        return value == null ? null : Measure.PIECE;
    }

    @TypeConverter
    public static String toString(Measure measure) {
        return measure == null ? null : measure.name();
    }
}
