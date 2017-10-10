package pl.pollub.android.shoppinglist.model.relation;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

import pl.pollub.android.shoppinglist.model.Buddy;
import pl.pollub.android.shoppinglist.model.BuddyGroup;

/**
 * @author Adrian
 * @since 2017-10-09
 */
public class BuddyGroupWithBuddies {
    @Embedded
    BuddyGroup buddyGroup;
    @Relation(parentColumn = "id", entityColumn = "buddy_id", entity = Buddy.class)
    List<Buddy> buddies;
}
