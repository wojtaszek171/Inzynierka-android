package pl.pollub.shoppinglist.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Adrian
 * @since 2017-12-13
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MiscUtils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager == null ? null : connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
