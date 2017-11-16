package pl.pollub.shoppinglist.model;

import com.parse.ParseUser;

import java.util.Date;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Adrian
 * @since 2017-10-26
 */
@EqualsAndHashCode(callSuper = true)
@ToString
public class User extends ParseUser {
    public static final String CLASS_NAME = "_User";

    public static final String KEY_LAST_ACTIVE_AT = "lastActiveAt";

    private Date lastActiveAt;

    public Date getLastActiveAt() {
        return getDate(KEY_LAST_ACTIVE_AT);
    }

    public void setLastActiveAt(Date lastActiveAt) {
        put(KEY_LAST_ACTIVE_AT, lastActiveAt);
    }
}
