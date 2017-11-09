package pl.pollub.shoppinglist.model.complextype;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Pawel
 * @since 2017-07-27
 */
public enum Status {
    ACTIVE,
    INACTIVE,
    TEMPLATE;

    public static final Status DEFAULT = ACTIVE;

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Converter {
        public static Status toStatus(String value) {
            try {
                return valueOf(value.toUpperCase());
            } catch (Exception e) {
                return DEFAULT;
            }
        }

        public static String toString(Status status) {
            return status == null ? null : status.name();
        }
    }
}
