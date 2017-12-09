package pl.pollub.shoppinglist.util.scheduling;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.util.Calendar;

import pl.pollub.shoppinglist.service.ScheduleService;

public class ScheduleClient {

    private ScheduleService boundService;
    private Context context;
    private boolean isBound;

    public ScheduleClient(Context context) {
        this.context = context;
    }

    /**
     * Call this to connect your activity to your service
     */
    public void doBindService() {
        context.bindService(new Intent(context, ScheduleService.class), connection, Context.BIND_AUTO_CREATE);
        isBound = true;
    }

    /**
     * When you attempt to connect to the service, this connection will be called with the result.
     * If we have successfully connected we instantiate our service object so that we can call methods on it.
     */
    private ServiceConnection connection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            boundService = ((ScheduleService.ServiceBinder) service).getService();
        }

        public void onServiceDisconnected(ComponentName className) {
            boundService = null;
        }
    };

    /**
     * Tell our service to set an alarm for the given date
     *
     * @param c a date to set the notification for
     */
    public void setAlarmForNotification(Calendar c, String title, String message) {
        boundService.setAlarm(c, title, message);
    }

    /**
     * When you have finished with the service call this method to stop it
     * releasing your connection and resources
     */
    public void doUnbindService() {
        if (isBound) {
            context.unbindService(connection);
            isBound = false;
        }
    }
}
