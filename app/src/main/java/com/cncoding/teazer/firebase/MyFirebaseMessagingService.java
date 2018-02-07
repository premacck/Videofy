package com.cncoding.teazer.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.cncoding.teazer.BaseBottomBarActivity;
import com.cncoding.teazer.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.messaging.RemoteMessage.Notification;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import static com.cncoding.teazer.BaseBottomBarActivity.NOTIFICATION_TYPE;
import static com.cncoding.teazer.BaseBottomBarActivity.SOURCE_ID;
import static com.cncoding.teazer.utilities.CommonUtilities.decodeUnicodeString;

/**
 *
 * Created by Prem $ on 11/9/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

//    private static final int STARTED_FOLLOWING = 1;
//    private static final int ACCEPTED_REQUEST = 2;
//    private static final int SENT_YOU_A_FOLLOW_REQUEST = 3;
//    private static final int ALSO_STARTED_FOLLOWING = 10;
//
//    private static final int LIKED_YOUR_VIDEO = 5;
//    private static final int POSTED_A_VIDEO = 7;
//    private static final int TAGGED_YOU_IN_A_VIDEO = 9;
//
//    private static final int REACTED_TO_YOUR_VIDEO = 4;
//    private static final int LIKED_YOUR_REACTION = 6;
//    private static final int REACTED_TO_A_VIDEO_THAT_YOU_ARE_TAGGED_IN = 8;

    private static final String TAG = "FirebaseMessagingSvc";
    private NotificationManager notificationManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Notification notification = remoteMessage.getNotification();

//        String imageUrl = remoteMessage.getData().get("image");
        if (notification != null) {
            Log.d(TAG, "From: " + remoteMessage.getFrom());
            Log.d(TAG, "Notification Message Body: " + notification.getBody());
            Log.d(TAG, "Notification Id: " + remoteMessage.getMessageId());


//            sendNotification(notification.getBody(), getBitmapFromUrl(imageUrl));
            for (Map.Entry<String,String> entry : remoteMessage.getData().entrySet()) {
                Log.d(TAG, "Notification Data: " + entry.getValue() + " at " + entry.getKey());
            }
            Log.d(TAG, "data: " + remoteMessage.getData());
            sendNotification(notification.getBody(), getBitmapFromUrl(remoteMessage.getData().get("thumb_url")), notification.getTitle(), remoteMessage.getData());
        }
    }

    private void sendNotification(String messageBody, Bitmap bitmap, String title, Map<String, String> data) {
        try {
            Intent intent = new Intent();
            intent.setClass(this, BaseBottomBarActivity.class);
//        if (messageBody.contains("follow"))
            Bundle bundle = new Bundle();
            if (data.get("source_id") != null) {
                bundle.putInt(SOURCE_ID, Integer.parseInt(data.get("source_id")));
            }
            bundle.putInt(NOTIFICATION_TYPE, Integer.parseInt(data.get("notification_type")));
            Log.d("FCMNOT", data.get("notification_type"));
            intent.putExtra("bundle", bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            initChannels();

            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "teazer_notification_01")
                    .setSmallIcon(R.drawable.ic_stat_notification_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setContentTitle(title != null ? decodeUnicodeString(title) : "Teazer")
                    .setContentText(decodeUnicodeString(messageBody))
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            if (bitmap != null)
                notificationBuilder.setLargeIcon(bitmap);

            if (notificationManager != null) {
                notificationManager.notify(0, notificationBuilder.build());
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

//    private int getAction(String type) {
//        try {
//            int notificationType = Integer.parseInt(type);
//            if (notificationType == 1 || notificationType == 2 || notificationType == 3 || notificationType == 10) {
//                return 4;
//            } else return 3;
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//            return 3;
//        }
//    }

    public void initChannels() {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationChannel channel = new NotificationChannel(
                "default",
                "TeazerNotificationChannel",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Teazer notification");
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }
    }

    /*
    *To get a Bitmap image from the URL received
    * */
    public Bitmap getBitmapFromUrl(String imageUrl) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(imageUrl).openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
