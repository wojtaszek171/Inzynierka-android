package pl.pollub.shoppinglist.model.relation;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

import pl.pollub.shoppinglist.model.ShoppingList;
import pl.pollub.shoppinglist.model.ShoppingListGroup;

/**
 * @author Adrian
 * @since 2017-10-09
 */
public class ShoppingListGroupWithShoppingLists {
    @Embedded
    ShoppingListGroup shoppingListGroup;
    @Relation(parentColumn = "id", entityColumn = "shopping_list_id", entity = ShoppingList.class)
    List<ShoppingList> shoppingLists;
}
