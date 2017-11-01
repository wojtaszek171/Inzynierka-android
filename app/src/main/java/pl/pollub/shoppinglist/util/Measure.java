package pl.pollub.shoppinglist.util;

import android.support.annotation.NonNull;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Pawel
 * @since 2017-07-27
 */
public enum Measure {
    PIECE,
    WEIGHT,
    LIQUID_CAPACITY,
    BOTTLE,
    BOX;

    public static final Measure DEFAULT = PIECE;

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Converter {
        public static Measure stringToMeasure(@NonNull String value) {
            try {
                return valueOf(value.toUpperCase());
            } catch (Exception e) {
                return DEFAULT;
            }
        }

        public static String measureToString(Measure measure) {
            return measure == null ? null : measure.name();
        }
    }
}
