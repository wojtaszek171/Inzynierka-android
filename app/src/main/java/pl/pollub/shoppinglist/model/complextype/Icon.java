package pl.pollub.shoppinglist.model.complextype;

import android.support.annotation.NonNull;
import android.util.Log;

import lombok.Getter;
import pl.pollub.shoppinglist.R;

/**
 * @author Pawel
 * @since 2017-07-28
 */
@Getter
public enum Icon {
    UNKNOWN(R.drawable.logo_big),
    BURGER(R.drawable.burger);

    public static final Icon DEFAULT = UNKNOWN;

    private final int resourceId;

    Icon(int resourceId) {
        this.resourceId = resourceId;
    }

    public static Icon fromString(@NonNull String value) {
        try {
            return valueOf(value.toUpperCase());
        } catch (Exception e) {
            Log.d("ComplexType", Icon.class.getSimpleName()
                    + ": " + value + " not found.");
            return DEFAULT;
        }
    }
}
