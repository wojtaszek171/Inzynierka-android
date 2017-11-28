package pl.pollub.shoppinglist.model;

import com.parse.ParseObject;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Adrian
 * @since 2017-10-26
 */
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseEntity extends ParseObject {
    @SuppressWarnings("unchecked")
    public <T extends ParseObject> T getEntity(String key) {
        ParseObject entity = super.getParseObject(key);

        if (entity == null) {
            return null;
        }

        // check if returned ParseObject is an instance of the specific Entity
        if (!(getClass().isInstance(entity))) {
            throw new RuntimeException("Entity is not an instance of " + getClass().getSimpleName());
        }

        return (T) entity;
    }

    public boolean equalsEntity(ParseObject obj) {
        if (!(getClass().isInstance(obj))) {
            return false;
        }

        return getObjectId().equals(obj.getObjectId());
    }
}
