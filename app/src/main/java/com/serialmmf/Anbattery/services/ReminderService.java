package com.serialmmf.Anbattery.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.serialmmf.Anbattery.R;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ReminderService extends IntentService {

    public static final int NOTIF_ID = 1;
    public static final String NOTIFICATION_CLICKED_ACTION = "ANBATTERY_NOTIFICATION_CLICKED";
    public static final String NOTIFICATION_DELETED_ACTION = "ANBATTERY_NOTIFICATION_DELETED";

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.raihanbd.booster.action.FOO";
    private static final String ACTION_BAZ = "com.raihanbd.booster.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.raihanbd.booster.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.raihanbd.booster.extra.PARAM2";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, ReminderService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, ReminderService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public ReminderService() {
        super("ReminderService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
// Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.reminder_layout_file);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.ic_launcher)
                        // Dismiss Notification
                .setAutoCancel(true)
                        // Set PendingIntent into Notification
                .setContentIntent(PendingIntent.getBroadcast(this, 0, new Intent(NOTIFICATION_CLICKED_ACTION),
                        PendingIntent.FLAG_UPDATE_CURRENT))
                        // Set DeleteIntent into Notification
                .setDeleteIntent(PendingIntent.getBroadcast(this, 0, new Intent(NOTIFICATION_DELETED_ACTION), 0))
                        // Set RemoteViews into Notification
                .setContent(remoteViews);

        // Locate and set the Image into custom_notification.xml ImageViews
        remoteViews.setImageViewResource(R.id.reminderimagenotileft,R.drawable.ic_launcher);
        //  remoteViews.setImageViewResource(R.id.imagenotiright, notificationBattery);

        // Locate and set the Text into custom_notification.xml TextViews
        remoteViews.setTextViewText(R.id.remindertitle, ""+getApplicationContext().getText(R.string.aboutTime));
        remoteViews.setTextViewText(R.id.remindertext,""+getApplicationContext().getText(R.string.optimizeNow));

        // Build Notification with Notification Manager
        notificationmanager.notify(NOTIF_ID, builder.build());
    }


    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
