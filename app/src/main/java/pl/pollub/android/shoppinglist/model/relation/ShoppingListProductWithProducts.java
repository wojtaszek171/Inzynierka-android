package pl.pollub.android.shoppinglist.model.relation;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

import pl.pollub.android.shoppinglist.model.Product;
import pl.pollub.android.shoppinglist.model.ShoppingListProduct;

/**
 * @author Adrian
 * @since 2017-10-08
 */
public class ShoppingListProductWithProducts {
    @Embedded
    ShoppingListProduct shoppingListProduct;
    @Relation(parentColumn = "id", entityColumn = "product_id", entity = Product.class)
    List<Product> products;
}
