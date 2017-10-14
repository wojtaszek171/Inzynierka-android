package pl.pollub.shoppinglist.model.relation;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

import pl.pollub.shoppinglist.model.ShoppingList;
import pl.pollub.shoppinglist.model.ShoppingListProduct;

/**
 * @author Adrian
 * @since 2017-10-08
 */
public class ShoppingListProductWithShoppingLists {
    @Embedded
    ShoppingListProduct shoppingListProduct;
    @Relation(parentColumn = "id", entityColumn = "shopping_list_id", entity = ShoppingList.class)
    List<ShoppingList> shoppingLists;
}
