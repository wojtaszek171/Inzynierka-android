package pl.pollub.shoppinglist.util;

import pl.pollub.shoppinglist.model.*;
import pl.pollub.shoppinglist.model.converter.BigDecimalConverter;
import pl.pollub.shoppinglist.model.converter.IconConverter;
import pl.pollub.shoppinglist.model.converter.LocalDateTimeConverter;
import pl.pollub.shoppinglist.model.converter.MeasureConverter;
import pl.pollub.shoppinglist.model.converter.StatusConverter;
import pl.pollub.shoppinglist.model.dao.*;

import android.arch.persistence.room.*;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

import org.threeten.bp.LocalDateTime;

import java.util.Random;

/**
 * @author Adrian
 * @since 2017-10-04
 */
@Database(version = 1, entities = {
        Buddy.class,
        BuddyGroup.class,
        Category.class,
        /*CustomProduct.class,*/
        Group.class,
        Product.class,
        ShoppingList.class,
        ShoppingListGroup.class,
        ShoppingListProduct.class,

})
@TypeConverters({
        BigDecimalConverter.class,
        IconConverter.class,
        LocalDateTimeConverter.class,
        MeasureConverter.class,
        StatusConverter.class
})
public abstract class AppDatabase extends RoomDatabase {
    public static final String DB_NAME = "app_db";

    private static AppDatabase instance;

    public abstract BuddyDao buddyDao();
    public abstract BuddyGroupDao buddygroupDao();
    public abstract CategoryDao categoryDao();
    public abstract GroupDao groupDao();
    public abstract ProductDao productDao();
    public abstract ShoppingListDao shoppingListDao();
    public abstract ShoppingListGroupDao shoppingListGroupDao();
    public abstract ShoppingListProductDao shoppingListProductDao();

    /**
     * Gets the singleton instance of AppDatabase
     *
     * @param context The context
     * @return The singleton instance of AppDatabase
     */
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            DB_NAME
                    ).build();
        }
        return instance;
    }

    /**
     * Switches the internal implementation with an empty in-memory database
     *
     * @param context The context
     */
    @VisibleForTesting
    public static void switchToInMemory(Context context) {
        instance = Room.inMemoryDatabaseBuilder(
                context.getApplicationContext(),
                AppDatabase.class
        ).build();
    }

    /**
     * Inserts the dummy data into the database if it is currently empty
     */
    public void populateInitialData() {
        /*if (productDao().count() == 0) {
            Product product;
            beginTransaction();
            try {
                for (int i = 0; i < 20; i++) {
                    product = new Product();
                    product.setName("Product no." + i);
                    product.setPredefined(true);
                    product.setDescription("This is just a desc of product " + i);
                    productDao().insert(product);
                }
                setTransactionSuccessful();
            } finally {
                endTransaction();
            }
        }*/

        if (buddyDao().count() == 0) {
            Buddy buddy;
            Random randomGen = new Random();
            beginTransaction();
            try {
                for (int i = 0; i < 15; i++) {
                    buddy = new Buddy();
                    buddy.setName("Friend no." + i);
                    buddy.setNickname("cool" + i + "ega" + (15-i));
                    buddy.setEmail("fr"+i+"@google.com");
                    buddy.setLastActiveAt(LocalDateTime.of(
                            2017,
                            randomGen.nextInt(12) + 1,
                            randomGen.nextInt(26) + 1,
                            randomGen.nextInt(24),
                            randomGen.nextInt(60)
                    ));
                    buddyDao().insert(buddy);
                }
                setTransactionSuccessful();
            } finally {
                endTransaction();
            }
        }
    }
}
