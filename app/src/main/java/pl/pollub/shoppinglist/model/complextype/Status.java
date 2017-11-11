package pl.pollub.shoppinglist.model.complextype;

/**
 * @author Pawel
 * @since 2017-07-27
 */
public enum Status {
    ACTIVE,
    INACTIVE,
    TEMPLATE;

    public static final Status DEFAULT = ACTIVE;

    public static Status fromString(String value) {
        try {
            return valueOf(value.toUpperCase());
        } catch (Exception e) {
            return DEFAULT;
        }
    }
}
