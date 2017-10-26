package pl.pollub.shoppinglist;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

/**
 * @author Adrian
 * @since 2017-09-17
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
    }
}
