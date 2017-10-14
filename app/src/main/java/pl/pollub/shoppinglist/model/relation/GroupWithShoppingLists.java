package pl.pollub.shoppinglist.model.relation;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

import pl.pollub.shoppinglist.model.Group;
import pl.pollub.shoppinglist.model.ShoppingList;

/**
 * @author Adrian
 * @since 2017-10-08
 */
public class GroupWithShoppingLists {
    @Embedded
    Group group;
    @Relation(parentColumn = "id", entityColumn = "shopping_list_id", entity = ShoppingList.class)
    List<ShoppingList> shoppingLists;
}
