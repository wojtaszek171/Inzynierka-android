package pl.pollub.shoppinglist.util;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Adrian
 * @since 2017-12-12
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ToastUtils {
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, @StringRes int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void showLongToast(Context context, @StringRes int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
    }
}
