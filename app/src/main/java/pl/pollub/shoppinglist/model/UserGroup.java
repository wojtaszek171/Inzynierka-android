package pl.pollub.shoppinglist.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import com.parse.ParseClassName;
import com.parse.ParseUser;

/**
 * Intermediate class for many-to-many relation between User and Group
 *
 * @author Adrian
 * @since 2017-10-27
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@ParseClassName(UserGroup.CLASS_NAME)
public class UserGroup extends BaseEntity {
    public static final String CLASS_NAME = "UserGroup";

    public static final String KEY_USER_POINTER = "userId";
    public static final String KEY_GROUP_POINTER = "groupId";
    public static final String KEY_USER_NICKNAME_WITHIN_GROUP = "userNicknameWithinGroup";
    public static final String KEY_USER_ADMINISTRATING_GROUP = "userAdministratingGroup";

    public ParseUser getUser() {
        return getEntity(KEY_USER_POINTER);
    }

    public Group getGroup() {
        return getEntity(KEY_GROUP_POINTER);
    }

    public String getUserNicknameWithinGroup() {
        return getString(KEY_USER_NICKNAME_WITHIN_GROUP);
    }

    public boolean getUserAdministratingGroup() {
        return getBoolean(KEY_USER_ADMINISTRATING_GROUP);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER_POINTER, user);
    }

    public void setGroup(Group group) {
        put(KEY_GROUP_POINTER, group);
    }

    public void setUserNicknameWithinGroup(String nicknameWithinGroup) {
        put(KEY_USER_NICKNAME_WITHIN_GROUP, nicknameWithinGroup);
    }

    public void setUserAdministratingGroup(boolean userAdministratingGroup) {
        put(KEY_USER_ADMINISTRATING_GROUP, userAdministratingGroup);
    }
}
