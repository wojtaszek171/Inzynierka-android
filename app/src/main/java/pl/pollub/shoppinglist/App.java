package pl.pollub.shoppinglist;

import android.app.Application;
import android.content.Context;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.parse.Parse;
import com.parse.ParseObject;

import pl.pollub.shoppinglist.model.*;

/**
 * @author Adrian
 * @since 2017-09-17
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
        registerParseApi(this);
    }

    private static void registerParseApi(Context context) {
        ParseObject.registerSubclass(Group.class);
        ParseObject.registerSubclass(CustomProduct.class);
        ParseObject.registerSubclass(ProductShoppingList.class);
        ParseObject.registerSubclass(ShoppingList.class);
        ParseObject.registerSubclass(UserGroup.class);

        if (!Parse.isLocalDatastoreEnabled()) {
            Parse.enableLocalDatastore(context);
        }

        Parse.initialize(context);
    }
}
