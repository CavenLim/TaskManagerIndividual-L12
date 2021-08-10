package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lvTasks;
    Button btnAddTask;

    ArrayList<Task> al ;
    ArrayAdapter<Task> aa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvTasks = findViewById(R.id.lvTasks);
        btnAddTask = findViewById(R.id.button);


        al=new ArrayList<Task>();
        aa= new ArrayAdapter<Task>(this,android.R.layout.simple_list_item_1,al);

        DBHelper dbh = new DBHelper(MainActivity.this);
        al.clear();
        al.addAll(dbh.getAllTasks());
        dbh.close();
        lvTasks.setAdapter(aa);


        Intent i = getIntent();
        int idToDelete =  i.getIntExtra("id",-1);
        String nameToDelete =  i.getStringExtra("taskDeletedName");



        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddActivity.class);
                startActivityForResult(intent,9);

            }
        });

        lvTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task target = al.get(position);
                Intent i = new Intent(MainActivity.this,EditDeleteActivity.class);
                i.putExtra("data",target);

                startActivityForResult(i,9);
            }
        });
        CharSequence reply = null;
        Intent intent = getIntent();
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            reply = remoteInput.getCharSequence("status");
        }

        if (reply != null) {


            if(reply.toString().equalsIgnoreCase("Completed") && idToDelete !=-1 && !nameToDelete.isEmpty()){
                DBHelper dbh2 = new DBHelper(MainActivity.this);
                String deletedTskName = nameToDelete;
                dbh2.deleteTask(idToDelete);
                al.clear();
                al.addAll(dbh2.getAllTasks());
                dbh2.close();
                aa.notifyDataSetChanged();
                NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancelAll();

                Toast.makeText(MainActivity.this, "You have Completed a task: "+deletedTskName ,
                        Toast.LENGTH_SHORT).show();


            }
        }



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 9){
            DBHelper dbh = new DBHelper(MainActivity.this);
            al.clear();
            al.addAll(dbh.getAllTasks());
            dbh.close();
            aa.notifyDataSetChanged();
        }
    }
}