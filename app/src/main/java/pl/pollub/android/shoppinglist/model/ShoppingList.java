package pl.pollub.android.shoppinglist.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import org.threeten.bp.LocalDateTime;
import lombok.*;
import pl.pollub.android.shoppinglist.util.Icon;
import pl.pollub.android.shoppinglist.util.Status;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * @author Pawel on 28.07.2017.
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Entity(tableName = "shopping_list", foreignKeys = {
        @ForeignKey(
                entity = Group.class,
                parentColumns = "id",
                childColumns = "group_id",
                onDelete = CASCADE,
                onUpdate = CASCADE
        ),
        @ForeignKey(
                entity = ShoppingListProduct.class,
                parentColumns = "id",
                childColumns = "shopping_list_product_id",
                onDelete = CASCADE,
                onUpdate = CASCADE
        )
})
public class ShoppingList extends NamedEntity {
    @ColumnInfo(name = "group_id", index = true)
    private int groupId;
    @ColumnInfo(name = "shopping_list_product_id", index = true)
    private int shoppingListProductId;
    private Icon icon;
    private Status status;
    @ColumnInfo(name = "last_synchronized_at")
    private LocalDateTime lastSynchronizedAt;

    public int getGroupId() {
        return groupId;
    }

    public int getShoppingListProductId() {
        return shoppingListProductId;
    }

    public Icon getIcon() {
        return icon;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getLastSynchronizedAt() {
        return lastSynchronizedAt;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public void setShoppingListProductId(int shoppingListProductId) {
        this.shoppingListProductId = shoppingListProductId;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setLastSynchronizedAt(LocalDateTime lastSynchronizedAt) {
        this.lastSynchronizedAt = lastSynchronizedAt;
    }
}
