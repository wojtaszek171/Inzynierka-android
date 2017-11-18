package pl.pollub.shoppinglist.model.complextype;

import android.support.annotation.NonNull;
import android.util.Log;

/**
 * @author Pawel
 * @since 2017-07-27
 */
public enum Status {
    ACTIVE,
    INACTIVE,
    TEMPLATE;

    public static final Status DEFAULT = ACTIVE;

    public static Status fromString(@NonNull String value) {
        try {
            return valueOf(value.toUpperCase());
        } catch (Exception e) {
            Log.d("ComplexType", Status.class.getSimpleName()
                    + ": " + value + " not found.");
            return DEFAULT;
        }
    }
}
