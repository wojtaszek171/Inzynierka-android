package pl.pollub.android.shoppinglist.db;

/**
 * @author Pawel on 27.07.2017.
 */

public enum Status {
    ACTIVE,
    INACTIVE,
    TEMPLATE;

    public static final Status DEFAULT_STATUS = Status.ACTIVE;
}
