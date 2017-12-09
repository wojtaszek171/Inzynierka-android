package pl.pollub.shoppinglist.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;

import pl.pollub.shoppinglist.util.scheduling.AlarmTask;

public class ScheduleService extends Service {

    private final IBinder binder = new ServiceBinder();


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("ScheduleService", "Received start id " + startId + ": " + intent);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void setAlarm(Calendar c, String title, String message) {
        new AlarmTask(this, c, title, message).run();
    }

    public class ServiceBinder extends Binder {
        public ScheduleService getService() {
            return ScheduleService.this;
        }
    }
}
