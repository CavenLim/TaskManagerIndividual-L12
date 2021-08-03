package com.example.taskmanager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;


public class NotificationReceiver extends BroadcastReceiver {
    int reqCode = 12345;
    String rec_data;
    ArrayList<Task> al ;

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        al=new ArrayList<Task>();
        DBHelper dbh = new DBHelper(context);
        al.clear();
        al.addAll(dbh.getAllTasks());
        dbh.close();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new
                    NotificationChannel("default", "Default Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.setDescription("This is for default notification");
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent2 = new Intent(context, AddActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity
                (context, reqCode, intent2,
                        PendingIntent.FLAG_CANCEL_CURRENT);

        Log.d("Received Msg : ",al.get(al.size()-1).getTask());
        Bitmap picture = BitmapFactory.decodeResource(context.getResources(), R.drawable.sentosa);
        // Build notification

        //Different Sound
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        Notification notification = new NotificationCompat.Builder(context, "default")
                .setSmallIcon(R.drawable.sentosa)
                .setContentTitle("Reminder : " + al.get(al.size()-1).getTask())
                .setContentText("Desc: " + al.get(al.size()-1).getDescription())
                .setLargeIcon(picture)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(picture)
                        .bigLargeIcon(null))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setSound(alarmSound)
                .build();


        // An integer good to have, for you to programmatically cancel it
        notificationManager.notify(12345, notification);
    }
}