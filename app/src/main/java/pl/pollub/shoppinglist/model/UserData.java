package pl.pollub.shoppinglist.model;

import com.parse.ParseClassName;
import com.parse.ParseRelation;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import lombok.ToString;

/**
 * Class containing extra data of particular user (such as list of inviters and friends)
 * Created once a user registers and cannot be reassigned
 * One user can only have one UserData entry at a time
 *
 * @author Adrian
 * @since 2017-11-19
 */
@ToString
@ParseClassName(UserData.CLASS_NAME)
public class UserData extends BaseEntity {
    public static final String CLASS_NAME = "UserData";

    public static final String KEY_INVITERS = "inviters";
    public static final String KEY_FRIENDS = "friends";
    public static final String KEY_CONVERSATIONS = "conversations";
    public static final String KEY_ASSIGNED = "assigned";

    public List<User> getInviters() {
        return getList(KEY_INVITERS);
    }

    public void setInviters(List<User> inviters) {
        put(KEY_INVITERS, inviters);
    }

    public boolean addUniqueInviter(User inviter) {
        addUnique(KEY_INVITERS, inviter);
        return true;
    }

    public boolean addUniqueInviters(Collection<User> inviters) {
        addAllUnique(KEY_INVITERS, inviters);
        return true;
    }

    public boolean removeInviter(User inviter) {
        removeAll(KEY_INVITERS, Collections.singletonList(inviter));
        return true;
    }

    public boolean removeInviters(Collection<User> inviters) {
        removeAll(KEY_INVITERS, inviters);
        return true;
    }

    public List<User> getFriends() {
        return getList(KEY_FRIENDS);
    }

    public void setFriends(List<User> friends) {
        put(KEY_FRIENDS, friends);
    }

    public boolean addUniqueFriend(User friend) {
        addUnique(KEY_FRIENDS, friend);
        return true;
    }

    public boolean addUniqueFriends(Collection<User> friends) {
        addAllUnique(KEY_FRIENDS, friends);
        return true;
    }

    public boolean removeFriend(User friend) {
        removeAll(KEY_FRIENDS, Collections.singletonList(friend));
        return true;
    }

    public boolean removeFriends(Collection<User> friends) {
        removeAll(KEY_FRIENDS, friends);
        return true;
    }

    public ParseRelation<Conversation> getConversationsRelation() {
        return getRelation(KEY_CONVERSATIONS);
    }

    public boolean isAssigned() {
        return getBoolean(KEY_ASSIGNED);
    }

    public void setAssigned(boolean assigned) {
        put(KEY_ASSIGNED, assigned);
    }
}
