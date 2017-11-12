package pl.pollub.shoppinglist.model;

import com.parse.ParseClassName;

import java.math.BigDecimal;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import pl.pollub.shoppinglist.model.complextype.PredefinedProduct;

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

    public static final String KEY_CUSTOM_PRODUCT_POINTER = "customProductId";
    public static final String KEY_PREDEFINED_PRODUCT = "predefinedProduct";
    public static final String KEY_SHOPPING_LIST_POINTER = "shoppingListId";
    public static final String KEY_UNIT_PRICE = "unitPrice";
    public static final String KEY_QUANTITY = "quantity";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_BOUGHT = "bought";

    public Product getProduct() {
        String predefinedProduct = getString(KEY_PREDEFINED_PRODUCT);

        return predefinedProduct != null
                ? PredefinedProduct.fromString(predefinedProduct)
                : getEntity(KEY_CUSTOM_PRODUCT_POINTER);
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

    public boolean isBought() {
        return getBoolean(KEY_BOUGHT);
    }

    public void setProduct(Product product) {
        if (product instanceof PredefinedProduct) {
            put(KEY_PREDEFINED_PRODUCT, product.toString());
        } else if (product instanceof CustomProduct) {
            put(KEY_CUSTOM_PRODUCT_POINTER, (CustomProduct) product);
        } else {
            throw new RuntimeException("setProduct unimplemented for " + product.getClass().getSimpleName());
        }
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
