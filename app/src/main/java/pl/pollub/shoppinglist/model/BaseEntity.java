package pl.pollub.shoppinglist.model;

import java.util.Date;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.parse.ParseObject;

/**
 * @author Adrian
 * @since 2017-10-26
 */
@EqualsAndHashCode(callSuper = true)
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
}
