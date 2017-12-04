package pl.pollub.shoppinglist.activity.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import pl.pollub.shoppinglist.R;

/**
 * Created by jrwoj on 30.11.2017.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute,true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView tv = getActivity().findViewById(R.id.listDeadlineTimepickerBtn);
        tv.setText(convertTimeToReadableString(hourOfDay, minute));
    }

    private String convertTimeToReadableString(int hour, int minute){
        StringBuilder timeBuilder = new StringBuilder();
        if(hour < 10){
            timeBuilder.append("0");
        }
        timeBuilder.append(hour);
        timeBuilder.append(":");
        if(minute < 10){
            timeBuilder.append("0");
        }
        timeBuilder.append(minute);
        return timeBuilder.toString();
    }
}
