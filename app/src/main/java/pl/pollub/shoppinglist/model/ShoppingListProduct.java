package pl.pollub.shoppinglist.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

import java.math.BigDecimal;

import lombok.*;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Intermediate table for many-to-many relation between ShoppingList and Product
 *
 * @author Pawel
 * @since 2017-07-28
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Entity(tableName = "shopping_list_product", foreignKeys = {
        @ForeignKey(
                entity = ShoppingList.class,
                parentColumns = "id",
                childColumns = "shopping_list_id",
                onDelete = CASCADE,
                onUpdate = CASCADE
        ),
        @ForeignKey(
                entity = Product.class,
                parentColumns = "id",
                childColumns = "product_id",
                onDelete = CASCADE,
                onUpdate = CASCADE
        )
})
public class ShoppingListProduct extends BaseEntity {
    @ColumnInfo(name = "shopping_list_id", index = true)
    private int shoppingListId;
    @ColumnInfo(name = "product_id", index = true)
    private int productId;
    private BigDecimal price;
    private int quantity;
    private String description;
    private boolean status;

    public int getShoppingListId() {
        return shoppingListId;
    }

    public int getProductId() {
        return productId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDescription() {
        return description;
    }

    public boolean isStatus() {
        return status;
    }

    public void setShoppingListId(int shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
