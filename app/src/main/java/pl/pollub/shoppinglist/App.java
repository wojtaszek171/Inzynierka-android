package pl.pollub.shoppinglist;

import android.app.Application;
import android.content.Context;

import com.jakewharton.threetenabp.AndroidThreeTen;

import pl.pollub.shoppinglist.model.*;

import static com.parse.Parse.enableLocalDatastore;
import static com.parse.Parse.initialize;
import static com.parse.Parse.isLocalDatastoreEnabled;
import static com.parse.ParseObject.registerSubclass;

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
        registerSubclass(User.class);
        registerSubclass(UserData.class);
        registerSubclass(Group.class);
        registerSubclass(CustomProduct.class);
        registerSubclass(ProductShoppingList.class);
        registerSubclass(ShoppingList.class);
        registerSubclass(UserGroup.class);

        if (!isLocalDatastoreEnabled()) {
            enableLocalDatastore(context);
        }

        initialize(context);
    }
}
