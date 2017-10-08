package pl.pollub.android.shoppinglist.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.icu.math.BigDecimal;

import org.threeten.bp.LocalDateTime;
import lombok.*;

/**
 * Intermediate table for many-to-many relation between ShoppingList and Product
 *
 * @author Pawel
 * @since 2017-07-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
public class ShoppingListProduct extends BaseEntity {
    @ColumnInfo(index = true)
    private int shoppingListId;
    @ColumnInfo(index = true)
    private int productId;
    private BigDecimal price;
    private int quantity;
    private String description;
    private boolean status;
}
