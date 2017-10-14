package pl.pollub.shoppinglist.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Intermediate table for many-to-many relation between ShoppingList and Group
 *
 * @author Adrian
 * @since 2017-10-08
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Entity(tableName = "shopping_list_group", foreignKeys = {
        @ForeignKey(
                entity = ShoppingList.class,
                parentColumns = "id",
                childColumns = "shopping_list_id",
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
public class ShoppingListGroup extends BaseEntity {
    @ColumnInfo(name = "shopping_list_id", index = true)
    private int shoppingListId;
    @ColumnInfo(name = "group_id", index = true)
    private int groupId;

    public int getShoppingListId() {
        return shoppingListId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setShoppingListId(int shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
