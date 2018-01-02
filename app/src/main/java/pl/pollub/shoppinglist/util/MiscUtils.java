package pl.pollub.shoppinglist.util;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.Random;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.pollub.shoppinglist.R;

/**
 * @author Adrian
 * @since 2017-12-13
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MiscUtils {
    private static final String[] COLOR_VALUES = {
            "#1abc9c", "#16a085", "#f1c40f", "#f39c12", "#2ecc71", "#27ae60",
            "#d35400", "#3498db", "#2980b9", "#e74c3c", "#c0392b", "#9b59b6",
            "#8e44ad", "#a94136", "#34495e", "#2c3e50", "#95a5a6", "#7f8c8d",
            "#ec87bf", "#d870ad", "#f69785", "#9ba37e", "#b49255", "#bdc3c7"
    };
    private static final Random GENERATOR = new Random();

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager == null ? null : connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static int getRandomAvatarBackgroundColor() {
        return Color.parseColor(
                COLOR_VALUES[GENERATOR.nextInt(COLOR_VALUES.length - 1)]
        );
    }

    public static void attachFragment(Fragment fragment, FragmentManager fm,
                                      @IdRes int containerViewId, boolean addToBackStack) {
        FragmentTransaction transaction = fm.beginTransaction();
        transaction
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(containerViewId, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}
