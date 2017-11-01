package pl.pollub.shoppinglist.model;

import java.util.Date;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import com.parse.ParseClassName;

/**
 * @author Adrian
 * @since 2017-10-26
 */
/*@EqualsAndHashCode(callSuper = true)
@ToString
@ParseClassName(Buddy.CLASS_NAME)*/
@Deprecated
public class Buddy extends BaseEntity {
    /*public static final String CLASS_NAME = "Buddy";

    public static final String KEY_LAST_ACTIVE_AT = "lastActiveAt";

    private String name;
    private String nickname;
    private String email;
    private Date lastActiveAt;

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public Date getLastActiveAt() {
        return getDate(KEY_LAST_ACTIVE_AT);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLastActiveAt(Date lastActiveAt) {
        put(KEY_LAST_ACTIVE_AT, lastActiveAt);
    }*/
}
