package pl.pollub.android.shoppinglist;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

/**
 * @author Adrian on 17.09.2017.
 */

public class ShoppingListApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
    }
}
