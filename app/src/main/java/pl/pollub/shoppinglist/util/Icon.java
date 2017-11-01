package pl.pollub.shoppinglist.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.pollub.shoppinglist.R;

/**
 * @author Pawel
 * @since 2017-07-28
 */
@Getter
public enum Icon {
    UNKNOWN(R.drawable.logo),
    BURGER(R.drawable.burger);

    public static final Icon DEFAULT = UNKNOWN;

    private final int resourceId;

    Icon(int resourceId) {
        this.resourceId = resourceId;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Converter {
        public static Icon stringToIcon(String value) {
            try {
                return valueOf(value.toUpperCase());
            } catch (Exception e) {
                return DEFAULT;
            }
        }

        public static String iconToString(Icon icon) {
            return icon == null ? null : icon.name();
        }
    }
}
