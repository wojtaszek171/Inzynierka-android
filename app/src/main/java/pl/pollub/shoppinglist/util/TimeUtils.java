package pl.pollub.shoppinglist.util;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.pollub.shoppinglist.R;

/**
 * @author Adrian
 * @since 2017-11-29
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeUtils {
    // TODO: https://stackoverflow.com/questions/15808333/get-real-time-not-device-set-time-in-android
    // http://tf.nist.gov/tf-cgi/servers.cgi

    public static String getRelativeTimeString(Context context, Date dateInFuture) {
        if (dateInFuture == null) {
            return context.getString(R.string.never);
        }

        Date currentTime = new Date();
        long differenceInMinutes = (currentTime.getTime() - dateInFuture.getTime()) / 60_000;
        differenceInMinutes = differenceInMinutes < 0 ? 0 : differenceInMinutes;
        long differenceInHours = differenceInMinutes / 60;
        long differenceInDays = differenceInHours / 24;
        String time = null;

        if (differenceInMinutes < 60) {
            time = differenceInMinutes + " " + context.getString(R.string.minutes_ago);
        } else if (differenceInMinutes >= 60 && differenceInHours < 24) {
            SimpleDateFormat formatter = new SimpleDateFormat("kk:mm");
            time = formatter.format(dateInFuture);
        } else if (differenceInHours >= 24 && differenceInDays < 7) {
            SimpleDateFormat formatter = new SimpleDateFormat("EEEE, kk:mm", Locale.getDefault());
            time = formatter.format(dateInFuture);
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm");
            time = formatter.format(dateInFuture);
        }

        return time;
    }
}
