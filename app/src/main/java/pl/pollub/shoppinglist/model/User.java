package pl.pollub.shoppinglist.model;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import bolts.Task;
import lombok.ToString;

/**
 * @author Adrian
 * @since 2017-10-26
 */
@ToString
@ParseClassName(User.CLASS_NAME)
public class User extends ParseUser {
    public static final String CLASS_NAME = "_User";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_USERDATA = "userData";
    public static final String KEY_LAST_ACTIVE_AT = "lastActiveAt";

    public static AtomicBoolean loggedIn = new AtomicBoolean(true);

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
        if (!loggedIn.get()) {
            return null;
        }

        User currentUser = (User) ParseUser.getCurrentUser();

        if (currentUser != null) {
            updateLastActiveTime(currentUser);
        }

        return currentUser;
    }

    private static void updateLastActiveTime(User user) {
        Task.callInBackground(() -> {
            Date currentTime = new Date();

            if (user.getLastActiveAt() != null) {
                long differenceInMinutes = (currentTime.getTime() - user.getLastActiveAt().getTime()) / 60_000;
                if (differenceInMinutes >= 5) {
                    user.setLastActiveAt(currentTime);
                } else {
                    throw new IllegalStateException("User was active in last 5 minutes");
                }
            } else {
                user.setLastActiveAt(currentTime);
            }

            return user;
        }).onSuccessTask(task -> task.getResult().saveInBackground()).continueWith(task -> {
            if (task.isFaulted() || task.isCancelled()) {
                Log.w("User", task.getError());
            }

            return null;
        });
    }

    public boolean equalsEntity(ParseObject obj) {
        if (!(obj instanceof ParseUser)) {
            return false;
        }

        ParseUser user = (ParseUser) obj;

        return getObjectId().equals(user.getObjectId());
    }
}
