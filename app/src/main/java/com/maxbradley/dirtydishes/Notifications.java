package com.maxbradley.dirtydishes;


/**
 * Created by Max on 5/9/16.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;


import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by Max on 5/9/16.
 */
public class Notifications extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

            Log.i("com.maxbradyly", "in notifications");

            Chore item = new Chore(intent);


            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(getNotificationIcon())
                            .setContentTitle("Task due soon!")
                            .setContentText(item.getTitle() + " due in 1 hour");

            // opens the main activity
            Intent resultIntent = new Intent(context, MainActivity.class);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(item.getID(), PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(item.getID(), mBuilder.build());
            Log.i("com.maxbradley", "Notifications has set the alarm");

    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.ic_notification : R.mipmap.ic_launcher;
    }
}
