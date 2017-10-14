package pl.pollub.android.shoppinglist.model.relation;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

import pl.pollub.android.shoppinglist.model.Buddy;
import pl.pollub.android.shoppinglist.model.Group;

/**
 * @author Adrian
 * @since 2017-10-08
 */
public class GroupWithBuddies {
    @Embedded
    Group group;
    @Relation(parentColumn = "id", entityColumn = "shopping_list_id", entity = Buddy.class)
    List<Buddy> buddies;
}