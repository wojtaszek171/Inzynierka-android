package pl.pollub.android.shoppinglist.util;

import pl.pollub.android.shoppinglist.model.*;
import pl.pollub.android.shoppinglist.model.dao.*;

import android.arch.persistence.room.*;

/**
 * @author Adrian
 * @since 2017-10-04
 */
@Database(version = 1, entities = {
        Buddy.class,
        Product.class,
        Category.class
})
abstract class AppDatabase extends RoomDatabase {
    abstract public BuddyDao buddyDao();
    abstract public ProductDao productDao();
    abstract public CategoryDao categoryDaoDao();
}
