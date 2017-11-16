package pl.pollub.shoppinglist.model;

import lombok.*;

/**
 * @author Adrian
 * @since 2017-10-26
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class NamedEntity extends BaseEntity {
    public static final String KEY_NAME = "name";
    public static final String KEY_DESCRIPTION = "description";

    public String getName() {
        return getString(KEY_NAME);
    }

    public void setName(String name) {
        put(KEY_NAME, name);
    }
}
