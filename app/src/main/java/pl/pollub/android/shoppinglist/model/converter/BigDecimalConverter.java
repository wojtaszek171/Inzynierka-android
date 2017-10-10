package pl.pollub.android.shoppinglist.model.converter;

import android.arch.persistence.room.TypeConverter;

import java.math.BigDecimal;

/**
 * @author Adrian
 * @since 2017-10-08
 */
public class BigDecimalConverter {
    @TypeConverter
    public static BigDecimal toBigDecimal(Integer value) {
        return (Integer) value == null ? null : new BigDecimal(value).movePointLeft(2);
    }

    @TypeConverter
    public static Integer toInteger(BigDecimal decimal) {
        return decimal == null ? null : decimal.movePointRight(2).intValue();
    }
}
