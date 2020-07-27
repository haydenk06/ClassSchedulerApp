package com.example.kelseyhaydenc196.Utilities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static android.content.Context.ALARM_SERVICE;
import static com.example.kelseyhaydenc196.Utilities.AlertReceiver.ALERT_NOTIFICATION_ID;
import static com.example.kelseyhaydenc196.Utilities.AlertReceiver.ALERT_TITLE_ID;

public class AlertController {
    private PendingIntent notifyPendingIntent;
    private Boolean isArmed;
    private AlarmManager am;
    private String alertTitle;
    private Date alertDate;
    private Context context;

    public AlertController(String alertTitle, Date alertDate, Context context) {
        this.isArmed = false;
        this.alertTitle = alertTitle;
        this.alertDate = alertDate;
        this.context = context;
        int notificationID = new Random().nextInt(10000);

        Intent intent = new Intent(context, AlertReceiver.class)
                .putExtra(ALERT_TITLE_ID, alertTitle)
                .putExtra(ALERT_NOTIFICATION_ID, notificationID);
        notifyPendingIntent = PendingIntent.getBroadcast(context, notificationID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        isArmed = (PendingIntent.getBroadcast(context, notificationID, intent, PendingIntent.FLAG_NO_CREATE) != null);
        am = (AlarmManager) context.getSystemService(ALARM_SERVICE);

    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setAlert() {
        String toastMessage;
        Calendar today = Calendar.getInstance();
        today.clear(Calendar.HOUR);
        today.clear(Calendar.HOUR_OF_DAY);
        today.clear(Calendar.MINUTE);
        today.clear(Calendar.SECOND);
        today.clear(Calendar.MILLISECOND);

        ///// checks SDK version /////
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Long mills = alertDate.getTime();
            am.set(AlarmManager.RTC_WAKEUP, alertDate.getTime(), notifyPendingIntent);
            toastMessage = alertTitle + " Notification is On!\nSet to " + alertDate.toString();
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
        }
    }
}
