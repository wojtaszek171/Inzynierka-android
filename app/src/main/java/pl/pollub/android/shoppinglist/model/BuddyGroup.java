package pl.pollub.android.shoppinglist.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Intermediate table for many-to-many relation between Buddy and Group
 *
 * @author Adrian
 * @since 2017-10-08
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Entity(tableName = BuddyGroup.TABLE_NAME, foreignKeys = {
        @ForeignKey(
                entity = Buddy.class,
                parentColumns = "id",
                childColumns = "buddy_id",
                onDelete = CASCADE,
                onUpdate = CASCADE
        ),
        @ForeignKey(
                entity = Group.class,
                parentColumns = "id",
                childColumns = "group_id",
                onDelete = CASCADE,
                onUpdate = CASCADE
        )
})
public class BuddyGroup extends BaseEntity {
    public static final String TABLE_NAME = "buddy_group";

    @ColumnInfo(name = "buddy_id", index = true)
    private int buddyId;
    @ColumnInfo(name = "group_id", index = true)
    private int groupId;
    @ColumnInfo(name = "local_buddy_nickname")
    private String localBuddyNickname;
    @ColumnInfo(name = "buddy_administrator")
    private boolean buddyAdministrator;

    public int getBuddyId() {
        return buddyId;
    }

    public int getGroupId() {
        return groupId;
    }

    public String getLocalBuddyNickname() {
        return localBuddyNickname;
    }

    public boolean isBuddyAdministrator() {
        return buddyAdministrator;
    }

    public void setBuddyId(int buddyId) {
        this.buddyId = buddyId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public void setLocalBuddyNickname(String localBuddyNickname) {
        this.localBuddyNickname = localBuddyNickname;
    }

    public void setBuddyAdministrator(boolean buddyAdministrator) {
        this.buddyAdministrator = buddyAdministrator;
    }
}
