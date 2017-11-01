package pl.pollub.shoppinglist.model;

import com.parse.ParseClassName;

import java.math.BigDecimal;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Intermediate class for many-to-many relation between Product and ShoppingList
 *
 * @author Adrian
 * @since 2017-10-29
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@ParseClassName(ProductShoppingList.CLASS_NAME)
public class ProductShoppingList extends BaseEntity {
    public static final String CLASS_NAME = "ProductShoppingList";

    public static final String KEY_PRODUCT_POINTER = "productId";
    public static final String KEY_SHOPPING_LIST_POINTER = "shoppingListId";
    public static final String KEY_UNIT_PRICE = "unitPrice";
    public static final String KEY_QUANTITY = "quantity";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_BOUGHT = "bought";

    public Product getProduct() {
        return getEntity(KEY_PRODUCT_POINTER);
    }

    public ShoppingList getShoppingList() {
        return getEntity(KEY_SHOPPING_LIST_POINTER);
    }

    public BigDecimal getUnitPrice() {
        long rawPrice = getLong(KEY_UNIT_PRICE);
        return rawPrice == 0 ? BigDecimal.ZERO : new BigDecimal(rawPrice).movePointLeft(2);
    }

    public int getQuantity() {
        return getInt(KEY_QUANTITY);
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public boolean getBought() {
        return getBoolean(KEY_BOUGHT);
    }

    public void setProduct(Product product) {
        put(KEY_PRODUCT_POINTER, product);
    }

    public void setShoppingList(ShoppingList shoppingList) {
        put(KEY_SHOPPING_LIST_POINTER, shoppingList);
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        put(KEY_UNIT_PRICE, unitPrice.movePointRight(2).longValue());
    }

    public void setQuantity(int quantity) {
        put(KEY_QUANTITY, quantity);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public void setBought(boolean bought) {
        put(KEY_BOUGHT, bought);
    }
}
