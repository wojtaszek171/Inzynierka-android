package pl.pollub.shoppinglist.model;

import java.util.Date;
import java.util.Objects;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.threeten.bp.LocalDateTime;
import com.parse.ParseObject;

import pl.pollub.shoppinglist.util.DateTimeWrapper;

/**
 * @author Adrian
 * @since 2017-10-26
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseEntity extends ParseObject {
    /*private DateTimeWrapper createdAt = new DateTimeWrapper();
    private DateTimeWrapper updatedAt = new DateTimeWrapper();*/

    @SuppressWarnings("unchecked")
    public <T extends ParseObject> T getEntity(String key) {
        ParseObject entity = Objects.requireNonNull(
                super.getParseObject(key), getClass().getSimpleName() + "can't be null");

        // check if returned ParseObject is an instance of the specific Entity
        if (!(getClass().isInstance(entity))) {
            throw new RuntimeException("Entity is not an instance of " + getClass().getSimpleName());
        }

        return (T) entity;
    }

    /*public LocalDateTime getUpdatedAtTimestamp() {
        Date currentUpdatedAt = super.getUpdatedAt();

        return currentUpdatedAt == null ? null : getCurrentTimestamp(updatedAt, currentUpdatedAt);
    }

    public LocalDateTime getCreatedAtTimestamp() {
        Date currentCreatedAt = super.getCreatedAt();

        return currentCreatedAt == null ? null : getCurrentTimestamp(createdAt, currentCreatedAt);
    }

    private static LocalDateTime getCurrentTimestamp(DateTimeWrapper cachedTimestamp, Date currentTimestamp) {
        if (currentTimestamp != null && cachedTimestamp.getDateTime().equals(currentTimestamp)) {
            return cachedTimestamp.getLocalDateTime();
        }
        // changes the mutable parameter of type DateTimeWrapper!
        cachedTimestamp.setDateTime(currentTimestamp);
        return cachedTimestamp.getLocalDateTime();
    }*/

    /**
     * @deprecated Deprecated, use {@link #getUpdatedAtTimestamp} instead.
     * @return Date
     */
    @Deprecated
    @Override
    public Date getUpdatedAt() {
        return super.getUpdatedAt();
    }

    /**
     * @deprecated Deprecated, use {@link #getCreatedAtTimestamp} instead.
     * @return Date
     */
    @Deprecated
    @Override
    public Date getCreatedAt() {
        return super.getCreatedAt();
    }
}
