package com.quirodev.sac.Link;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.quirodev.sac.R;

import static android.support.v4.app.NotificationCompat.DEFAULT_SOUND;
import static android.support.v4.app.NotificationCompat.DEFAULT_VIBRATE;


public class MyFirebaseMessagingService  extends FirebaseMessagingService {

    private String TAG = "Message Service";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            String title=remoteMessage.getData().get("title");
            String body=remoteMessage.getData().get("body");
            sendNotification(title , body);
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE );
            PowerManager.WakeLock wakeLock = pm.newWakeLock( PowerManager.SCREEN_DIM_WAKE_LOCK
                    | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG" );
            wakeLock.acquire(3000);

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                // scheduleJob();

            } else {
                // Handle message within 10 seconds
                // handleNow();
            }
        }
        super.onMessageReceived(remoteMessage);
    }

    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(this, ResultActivity.class); // class which has to be redirected when user clicks notification
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSmallIcon(R.mipmap.ic_launcher) // your drawable for notification icon
                .setContentTitle(title)
                .setContentText(messageBody)
                .setSound(defaultSoundUri)
                .setVibrate(new long[]{100,0,100,0})
                .setDefaults(DEFAULT_SOUND|DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent);



        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());


    }
}