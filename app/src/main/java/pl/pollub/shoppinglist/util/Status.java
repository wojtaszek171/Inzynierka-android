package pl.pollub.shoppinglist.util;

/**
 * @author Pawel
 * @since 2017-07-27
 */
public enum Status {
    ACTIVE,
    INACTIVE,
    TEMPLATE;

    public static final Status DEFAULT_STATUS = Status.ACTIVE;
}
