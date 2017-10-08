package pl.pollub.android.shoppinglist.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import org.threeten.bp.LocalDateTime;
import lombok.*;
import pl.pollub.android.shoppinglist.util.Status;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * @author Pawel on 28.07.2017.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(foreignKeys = {
        @ForeignKey(
                entity = Group.class,
                parentColumns = "id",
                childColumns = "shopping_group_id",
                onDelete = CASCADE,
                onUpdate = CASCADE
        )
})
public class ShoppingList extends NamedEntity {
    private int shoppingGroupId;
    private int shoppingListProductId;
    private String icon;
    private Status status;
    private LocalDateTime lastSynchronizedAt;
}
