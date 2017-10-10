package pl.pollub.android.shoppinglist.model.relation;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

import pl.pollub.android.shoppinglist.model.Group;
import pl.pollub.android.shoppinglist.model.ShoppingListGroup;

/**
 * @author Adrian
 * @since 2017-10-09
 */
public class ShoppingListGroupWithGroups {
    @Embedded
    ShoppingListGroup shoppingListGroup;
    @Relation(parentColumn = "id", entityColumn = "group_id", entity = Group.class)
    List<Group> groups;
}
