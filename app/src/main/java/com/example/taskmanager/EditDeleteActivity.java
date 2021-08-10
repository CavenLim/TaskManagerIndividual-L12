package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class EditDeleteActivity extends AppCompatActivity {
    int requestCode = 123;
    Button btnEditTask,btnDelete;
    EditText etTask,etDesc,etTime;
    Task data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete);

        btnEditTask = findViewById(R.id.btnEditTask);
        btnDelete = findViewById(R.id.btnDelete);
        etTask = findViewById(R.id.etEditName);
        etDesc = findViewById(R.id.etEditDescription);
        etTime = findViewById(R.id.etEditSeconds);

        Intent i = getIntent();
        data = (Task) i.getSerializableExtra("data");

        etTask.setText(data.getTask());
        etDesc.setText(data.getDescription());


        btnEditTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!etTask.getText().toString().isEmpty() && !etDesc.getText().toString().isEmpty() && !etTime.getText().toString().isEmpty()){
                    DBHelper dbh = new DBHelper(EditDeleteActivity.this);


                    data.setTask(etTask.getText().toString());
                    data.setDescription(etDesc.getText().toString());
                    long inserted_id = dbh.updateTask(data);
                    dbh.close();

                    if (inserted_id != -1){
                        Toast.makeText(EditDeleteActivity.this, "Edit successful",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditDeleteActivity.this,MainActivity.class);

                        setResult(RESULT_OK, intent);

                        Calendar cal  = Calendar.getInstance();
                        cal.add(Calendar.SECOND,Integer.parseInt(etTime.getText().toString()));

                        Intent intent2 = new Intent(EditDeleteActivity.this,NotificationReceiver.class);
                        intent2.putExtra("taskId",data.getId());
                        intent2.putExtra("taskName",etTask.getText().toString());
                        intent2.putExtra("taskDesc",etDesc.getText().toString());

                        PendingIntent pendingIntent =  PendingIntent.getBroadcast(EditDeleteActivity.this,requestCode,intent2,PendingIntent.FLAG_CANCEL_CURRENT);

                        AlarmManager am  = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
                        am.set(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),pendingIntent);
                        finish();


                    }
                }
                else{
                    Toast.makeText(EditDeleteActivity.this,"All fields required",Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbh = new DBHelper(EditDeleteActivity.this);
                dbh.deleteTask(data.getId());
                dbh.close();
                Intent intent = new Intent(EditDeleteActivity.this,MainActivity.class);

                setResult(RESULT_OK, intent);
                finish();
            }
        });


    }
}