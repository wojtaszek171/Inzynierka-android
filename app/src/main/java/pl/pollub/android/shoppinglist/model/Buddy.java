package pl.pollub.android.shoppinglist.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import org.threeten.bp.LocalDateTime;
import lombok.*;

/**
 * @author Adrian
 * @since 2017-10-07
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Entity(tableName = "buddy")
public class Buddy extends BaseEntity {
    private String name;
    private String nickname;
    private String email;
    @ColumnInfo(name = "last_active_at")
    private LocalDateTime lastActiveAt;

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getLastActiveAt() {
        return lastActiveAt;
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

    public void setLastActiveAt(LocalDateTime lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }
}
