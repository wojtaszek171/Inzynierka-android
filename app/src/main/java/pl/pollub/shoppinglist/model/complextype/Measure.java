package pl.pollub.shoppinglist.model.complextype;

import android.support.annotation.NonNull;
import android.util.Log;

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

    public static Measure fromString(@NonNull String value) {
        try {
            return valueOf(value.toUpperCase());
        } catch (Exception e) {
            Log.d("ComplexType", Measure.class.getSimpleName()
                    + ": " + value + " not found.");
            return DEFAULT;
        }
    }
}
