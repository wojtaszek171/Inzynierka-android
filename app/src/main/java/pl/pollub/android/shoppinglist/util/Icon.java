package pl.pollub.android.shoppinglist.util;

import lombok.Getter;
import pl.pollub.android.shoppinglist.R;

/**
 * @author Pawel
 * @since 2017-07-28
 */
@Getter
public enum Icon {
    UNKNOWN(R.drawable.logo),
    BURGER(R.drawable.burger);

    private final int resourceId;

    Icon(int resourceId) {
        this.resourceId = resourceId;
    }
}
