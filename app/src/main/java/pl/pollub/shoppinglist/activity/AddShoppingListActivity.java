package pl.pollub.shoppinglist.activity;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.activity.fragment.DatePickerFragment;
import pl.pollub.shoppinglist.activity.fragment.TimePickerFragment;
import pl.pollub.shoppinglist.util.MiscUtils;
import pl.pollub.shoppinglist.util.ToastUtils;
import pl.pollub.shoppinglist.util.scheduling.ScheduleClient;

public class AddShoppingListActivity extends AppCompatActivity {
    private static int id;
    private Button textDate;
    private boolean template;
    private ParseObject listTemplate;
    private ParseObject list;
    private ParseObject listObject;
    private EditText listName;
    private String listNameString;
    private EditText description;
    private String descriptionString;

    private Button timepickerBtn;
    ToggleButton setNotificationsToggle;

    private DatePicker datePicker;
    private ScheduleClient scheduleClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shopping_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        scheduleClient = new ScheduleClient(this);
        scheduleClient.doBindService();

        initLayoutComponets();

        if (template) {
            setTitle(R.string.addTemplate);
            textDate.setVisibility(View.GONE);
        } else if (listTemplate != null) {
            setTitle("Podaj szczegóły listy");
        } else if (listObject != null) {
            setTitle("Edytuj listę");
        } else {
            setTitle(R.string.addList);
        }
        Button saveNewList = findViewById(R.id.saveNewList);
        TextView setDeadline = findViewById(R.id.listDeadline);

        if (listObject != null) {
            listName.setText(listObject.getString("name"));
            description.setText(listObject.getString("description"));

            if (!listObject.getString("deadline")
                    .equals(getString(R.string.date) + " " + getString(R.string.time))) {
                String[] dateAndTimeString = listObject.getString("deadline").split(" ");
                textDate.setText(dateAndTimeString[0]);
                timepickerBtn.setText(dateAndTimeString[1]);
            }
        }

        saveNewList.setOnClickListener(view -> {
            listNameString = listName.getText().toString().trim();
            descriptionString = description.getText().toString().trim();
            if (listNameString.isEmpty()) {
                Toast.makeText(this, "Musisz podać nazwę listy", Toast.LENGTH_SHORT).show();
            } else {
                if (listObject != null) {
                    updateShoppingList();
                } else {
                    createShoppingList();
                }
            }
        });

        setDeadline.setOnClickListener(view -> datePickerDialog());
    }

    @Override
    protected void onStop() {
        // When our activity is stopped ensure we also stop the connection to the service
        // this stops us leaking our activity into the system *bad*
        if (scheduleClient != null)
            scheduleClient.doUnbindService();
        super.onStop();
    }

    private void initLayoutComponets() {
        id = getIntent().getIntExtra("LOCAL_LIST_ID", 1);
        template = getIntent().getBooleanExtra("TEMPLATE", false);
        listTemplate = getIntent().getParcelableExtra("LIST_TEMPLATE");
        listObject = getIntent().getParcelableExtra("LIST_OBJECT");
        listName = findViewById(R.id.listName);
        description = findViewById(R.id.list_description);

        timepickerBtn = findViewById(R.id.listDeadlineTimepickerBtn);
        timepickerBtn.setOnClickListener(this::showTimePickerDialog);

        setNotificationsToggle = findViewById(R.id.toggleNotifications);
        setNotificationsToggle.setOnCheckedChangeListener((buttonView, isChecked) -> setNotificationSetButtonsVisibility(isChecked));
        textDate = findViewById(R.id.listDeadline);
    }

    private void updateShoppingList() {
        if (setNotificationsToggle.isChecked()) {
            if (textDate.getText().toString().equals(getString(R.string.date))
                    || timepickerBtn.getText().toString().equals(getString(R.string.time))) {
                ToastUtils.showLongToast(this, "Wybierz poprawną datę/czas lub wyłącz powiadomienie");
                return;
            }

            setNotification();
        }

        if (MiscUtils.isNetworkAvailable(this) && ParseUser.getCurrentUser() != null) {
            listObject.put("name", listNameString);
            listObject.put("status", "0");
            listObject.put("deadline", textDate.getText().toString());
            listObject.put("description", descriptionString);
            listObject.put("isTemplate", template);
            listObject.saveEventually();
            listObject.pinInBackground(ex -> {
                if (ex == null) {
                    finish();
                }
            });
        } else {
            ParseQuery<ParseObject> offlineListToUpdateQuery = ParseQuery.getQuery("ShoppingList");
            offlineListToUpdateQuery.whereEqualTo("localId", listObject.getString("localId"));
            offlineListToUpdateQuery.fromLocalDatastore();
            offlineListToUpdateQuery.findInBackground((resultList, e) -> {
                if (e == null) {
                    ParseObject listToUpdate = resultList.get(0);
                    listObject.put("name", listNameString);
                    listObject.put("status", "0");
                    listObject.put("deadline", textDate.getText().toString() + " "
                            + timepickerBtn.getText().toString());
                    listObject.put("description", descriptionString);
                    listObject.put("isTemplate", template);
                }
            });
        }
    }

    private void setNotificationSetButtonsVisibility(boolean show) {
        Button datepickerBtnLocal = findViewById(R.id.listDeadline);
        Button timepickerBtnLocal = findViewById(R.id.listDeadlineTimepickerBtn);

        if (show) {
            datepickerBtnLocal.setVisibility(View.VISIBLE);
            timepickerBtnLocal.setVisibility(View.VISIBLE);
        } else {
            datepickerBtnLocal.setVisibility(View.INVISIBLE);
            timepickerBtnLocal.setVisibility(View.INVISIBLE);
        }
    }

    private Calendar getCalendarForNotification() {
        Calendar calendar = Calendar.getInstance();
        TextView datepickerBtnLocal = findViewById(R.id.listDeadline);
        TextView timepickerBtnLocal = findViewById(R.id.listDeadlineTimepickerBtn);
        String[] date, time;
        int year, month, day;
        int hour, minute;
        date = datepickerBtnLocal.getText().toString().split("-");
        time = timepickerBtnLocal.getText().toString().split(":");

        year = Integer.parseInt(date[0]);
        month = Integer.parseInt(date[1]);
        day = Integer.parseInt(date[2]);

        hour = Integer.parseInt(time[0]);
        minute = Integer.parseInt(time[1]);
        calendar.set(year, month - 1, day, hour, minute);

        return calendar;
    }

    private void setNotification() {
        Calendar notifyCalendar = getCalendarForNotification();

        String notificationTitle = getApplicationInfo().loadLabel(getPackageManager()).toString();
        String notificationMessage = "Wykup listę " + listNameString + "!"
                + " - " + notifyCalendar.get(Calendar.DAY_OF_MONTH) + "-" + notifyCalendar.get(Calendar.MONTH) +
                "-" + notifyCalendar.get(Calendar.YEAR) + " " + notifyCalendar.get(Calendar.HOUR) + ":"
                + notifyCalendar.get(Calendar.MINUTE);

        scheduleClient.setAlarmForNotification(notifyCalendar, notificationTitle, notificationMessage);
    }

    private void showTimePickerDialog(View v) {
        DialogFragment dFragment = new TimePickerFragment();
        dFragment.show(getFragmentManager(), "Time Picker");
    }

    private void datePickerDialog() {
        DialogFragment dFragment = new DatePickerFragment();
        dFragment.show(getFragmentManager(), "Date Picker");
    }

    private void createShoppingList() {
        if (setNotificationsToggle.isChecked()) {
            if (textDate.getText().toString().equals(getString(R.string.date))
                    || timepickerBtn.getText().toString().equals(getString(R.string.time))) {
                ToastUtils.showLongToast(this, "Wybierz poprawną datę/czas lub wyłącz powiadomienie");
                return;
            }

            setNotification();
        }

        list = ParseObject.create("ShoppingList");

        list.put("name", listNameString);
        list.put("status", "0");
        list.put("deadline", textDate.getText().toString() + " "
                + timepickerBtn.getText().toString());
        list.put("description", descriptionString);
        list.put("isTemplate", template);

        if (ParseUser.getCurrentUser() != null) {
            String user = ParseUser.getCurrentUser().getUsername();
            list.put("belongsTo", ParseUser.getCurrentUser().getUsername());
            list.put("localId", user + Integer.toString(id));
            list.put("description", "opis");
            list.add("sharedAmong", ParseUser.getCurrentUser().getUsername());
            list.saveEventually(e -> {
                if (e == null) {
                    // No error, the object was saved
                    // Toast.makeText(getApplicationContext(), "Complete", Toast.LENGTH_LONG).show();
                } else {
                    // Error saving object, print the logs
                    e.printStackTrace();
                }
            });

        } else {
            list.put("localId", Integer.toString(id));
        }
        list.pinInBackground(e -> {
            if (e == null) {
                if (listTemplate != null) {
                    recoveryTempalte(listTemplate, list.get("localId"));
                } else {
                    Intent intent = new Intent(AddShoppingListActivity.this, ShoppingListDetailsActivity.class);
                    intent.putExtra("LIST_OBJECT", list);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public void setIdForLocal() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("localId");
        query.fromLocalDatastore();
        query.findInBackground((scoreList, exception) -> {
            if (exception == null) {
                for (ParseObject s : scoreList) {
                    id = s.getInt("id");
                    s.increment("id");
                    s.pinInBackground();
                }
                Log.d("score", "Retrieved " + scoreList.size() + " scores");
                if (scoreList.size() == 0) {
                    ParseObject localId = new ParseObject("localId");
                    localId.put("id", 1);
                    localId.pinInBackground();
                    id = 1;
                }
            } else {
                Log.d("score", "Error: " + exception.getMessage());
            }
        });
    }

    private void recoveryTempalte(ParseObject listTemplate, Object localIdd) {

        List<HashMap> templateProducts = listTemplate.getList("nestedProducts");
        for (HashMap product : templateProducts) {
            if (ParseUser.getCurrentUser() != null) {
                String user = ParseUser.getCurrentUser().getUsername();
                product.put("localId", user + id);
                product.put("belongsTo", localIdd);
            } else {
                product.put("belongsTo", localIdd);
                product.put("localId", id);
            }

            ParseQuery<ParseObject> queryy = ParseQuery.getQuery("localId");
            queryy.fromLocalDatastore();
            queryy.findInBackground((scoreListt, e) -> {
                if (e == null) {
                    for (ParseObject item : scoreListt) {
                        item.put("id", id);
                        item.pinInBackground();
                    }
                }
            });
        }
        list.put("nestedProducts", templateProducts);
        if (ParseUser.getCurrentUser() != null) {
            list.saveEventually();
        }
        list.pinInBackground();
        Intent intent = new Intent(getApplicationContext(), ShoppingListDetailsActivity.class);
        intent.putExtra("LIST_OBJECT", list);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
