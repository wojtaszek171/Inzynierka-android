package pl.pollub.shoppinglist.activity.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import pl.pollub.shoppinglist.R;

/**
 * @author Jakub
 * @since 2017-11-30
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute, true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar calendar = getCalendarForNotification(hourOfDay, minute);
        if (calendar != null) {
            if (calendar.getTime().after(Calendar.getInstance().getTime())) {
                TextView tv = getActivity().findViewById(R.id.listDeadlineTimepickerBtn);
                tv.setText(convertTimeToReadableString(hourOfDay, minute));
            } else {
                CharSequence text = "Nie możesz ustawić przypomnienia w przeszłości!";
                Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
                toast.show();
            }
        }

    }

    private String convertTimeToReadableString(int hour, int minute) {
        StringBuilder timeBuilder = new StringBuilder();
        if (hour < 10) {
            timeBuilder.append("0");
        }
        timeBuilder.append(hour);
        timeBuilder.append(":");
        if (minute < 10) {
            timeBuilder.append("0");
        }
        timeBuilder.append(minute);
        return timeBuilder.toString();
    }

    private Calendar getCalendarForNotification(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        TextView datepickerBtnLocal = getActivity().findViewById(R.id.listDeadline);
        String[] date;
        int year, month, day;

        date = datepickerBtnLocal.getText().toString().split("-");

        try {
            year = Integer.parseInt(date[0]);
            month = Integer.parseInt(date[1]);
            day = Integer.parseInt(date[2]);
            calendar.set(year, month - 1, day, hour, minute, 0);
        } catch (NumberFormatException nfe) {
            CharSequence text = "Najpierw ustaw datę przypomnienia!";
            Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
            toast.show();
            calendar = null;
        }


        return calendar;
    }
}
