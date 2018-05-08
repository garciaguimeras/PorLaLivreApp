package dev.blackcat.porlalivre.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationHelper
{

    public static int NOTIFICATION_ID = 0;

    NotificationCompat.Builder notificationBuilder;
    Context context;

    public NotificationHelper(Context context)
    {
        this.context = context;
    }

    public int createNotification(String title, String text, int icon, Intent intent, boolean cancellable)
    {
        return createNotification(title, text, new String[] {}, icon, intent, cancellable);
    }

    public synchronized int createNotification(String title, String text, String[] lines, int icon, Intent intent, boolean cancellable)
    {
        NOTIFICATION_ID++;

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setSmallIcon(icon);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(text);
        if (lines.length > 0)
        {
            NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
            for (String line: lines)
                style.addLine(line);
            notificationBuilder.setStyle(style);
        }
        notificationBuilder.setContentIntent(pendingIntent);
        notify(NOTIFICATION_ID, cancellable);
        return NOTIFICATION_ID;
    }

    public void updateNotification(int id, String text, int icon, boolean cancellable)
    {
        notificationBuilder.setContentText(text);
        notificationBuilder.setSmallIcon(icon);
        notify(id, cancellable);
    }

    public void updateNotificationProgress(int id, int max, int progress, boolean cancellable)
    {
        notificationBuilder.setProgress(max, progress, false);
        notify(id, cancellable);
    }

    public void notify(int id, boolean cancellable)
    {
        Notification notification = notificationBuilder.build();

        /*
	    if (!cancellable)
	    	notification.flags = Notification.DEFAULT_ALL | Notification.FLAG_FOREGROUND_SERVICE;
	    else
	    	notification.flags = Notification.DEFAULT_ALL;
        */

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(id, notification);
    }

    public void cancelNotification(int id)
    {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }
}
