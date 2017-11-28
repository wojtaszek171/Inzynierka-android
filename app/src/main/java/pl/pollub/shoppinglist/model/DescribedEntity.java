package pl.pollub.shoppinglist.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Adrian
 * @since 2017-11-12
 */
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class DescribedEntity extends NamedEntity {
    public static final String KEY_DESCRIPTION = "description";

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }
}

