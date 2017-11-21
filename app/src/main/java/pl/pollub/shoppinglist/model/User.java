package pl.pollub.shoppinglist.model;

import com.parse.ParseClassName;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Adrian
 * @since 2017-10-26
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@ParseClassName(User.CLASS_NAME)
public class User extends ParseUser {
    public static final String CLASS_NAME = "_User";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_USERDATA = "userData";
    public static final String KEY_LAST_ACTIVE_AT = "lastActiveAt";

    public Date getLastActiveAt() {
        return getDate(KEY_LAST_ACTIVE_AT);
    }

    public void setLastActiveAt(Date lastActiveAt) {
        put(KEY_LAST_ACTIVE_AT, lastActiveAt);
    }

    public UserData getUserData() {
        return (UserData) getParseObject(KEY_USERDATA);
    }

    public void setUserData(UserData userData) {
        if (userData.isAssigned()) {
            throw new IllegalArgumentException("UserData instance cannot be assigned to more than one User");
        }

        userData.setAssigned(true);
        put(KEY_USERDATA, userData);
    }

    public static User getCurrentUser() {
        return (User) ParseUser.getCurrentUser();
    }
}
