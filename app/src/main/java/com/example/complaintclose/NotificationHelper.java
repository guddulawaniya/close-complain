package com.example.complaintclose;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.SystemClock;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationHelper {


    private static final String CHANNEL_ID = "progress_channel";
    private static final int NOTIFICATION_ID = 1;
    private static final int maxprogress = 100;


    private NotificationManagerCompat notificationManager;

    public NotificationHelper(Context context) {
        notificationManager = NotificationManagerCompat.from(context);

        // Create a notification channel for Android Oreo and above
        createNotificationChannel(context);

    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Progress Channel";
            String description = "Channel for progress notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void showProgressNotification(Context context ,String notifionationdata) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.notifications)
                .setContentTitle(notifionationdata)
                .setContentText("Uploading in progress...")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setProgress(maxprogress, 0, false);

        Notification notification = builder.build();

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    public void updateprogressbar(Context context,String notifionationdata) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.notifications)
                .setContentTitle(notifionationdata)
                .setContentText("Succesfully Close Complain")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        Notification notification = builder.build();


        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(2000);
                for (int currentProgress = 0; currentProgress <= 100; currentProgress += 10) {

                    builder.setProgress(maxprogress, currentProgress, false);
                    if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    notificationManager.notify(NOTIFICATION_ID, notification);
                    SystemClock.sleep(1000);


                }
                builder.setContentText("Download Finished")
                        .setProgress(0, 0, false);
                notificationManager.notify(NOTIFICATION_ID, notification);

            }
        }).start();
    }
    public void dismissNotification() {
        notificationManager.cancel(NOTIFICATION_ID);
    }
}
