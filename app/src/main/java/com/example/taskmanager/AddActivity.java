package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class AddActivity extends AppCompatActivity {
    Button btnAddTask,btnCancel;
    EditText etTask,etDesc,etTime;
    int requestCode = 123;
    int notificationId = 888;
    public static final String BROADCAST = "com.example.taskmanager.android.action.broadcast";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        btnAddTask = findViewById(R.id.btnAddTask);
        btnCancel = findViewById(R.id.btnCancel);
        etTask = findViewById(R.id.etName);
        etDesc = findViewById(R.id.etDescription);
        etTime = findViewById(R.id.etSeconds);


        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!etTask.getText().toString().isEmpty() && !etDesc.getText().toString().isEmpty() && !etTime.getText().toString().isEmpty()){
                    String taskTitle = etTask.getText().toString();
                    String taskDesc = etDesc.getText().toString();
                    ArrayList<Task> al  = new ArrayList<>();
                    DBHelper dbh = new DBHelper(AddActivity.this);
                    al.addAll(dbh.getAllTasks());
                    long inserted_id = dbh.insertTask(taskTitle,taskDesc);
                    dbh.close();

                    if (inserted_id != -1){
                        Toast.makeText(AddActivity.this, "Insert successful",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddActivity.this,MainActivity.class);


                        setResult(RESULT_OK, intent);


                        Calendar cal  = Calendar.getInstance();
                        cal.add(Calendar.SECOND,Integer.parseInt(etTime.getText().toString()));

                        Intent intent2 = new Intent(AddActivity.this,NotificationReceiver.class);
                        intent2.putExtra("taskId",al.get(al.size()-1).getId());
                        intent2.putExtra("taskName",etTask.getText().toString());
                        intent2.putExtra("taskDesc",etDesc.getText().toString());

                        PendingIntent pendingIntent =  PendingIntent.getBroadcast(AddActivity.this,requestCode,intent2,PendingIntent.FLAG_CANCEL_CURRENT);

                        AlarmManager am  = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
                        am.set(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),pendingIntent);
                        finish();


                    }
                }
                else{
                    Toast.makeText(AddActivity.this,"All fields required",Toast.LENGTH_SHORT).show();
                }


            }


        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}