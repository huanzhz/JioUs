package com.jious.EventActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.text.util.*;
import com.jious.R;
import com.jious.Model.Event;

public class EventCreation extends AppCompatActivity {

    EditText eName,eDes,eLocation,sDate,sTime,eDate,eTime;
    Button Create;


     FirebaseUser fireUser;
    private DatabaseReference databaseEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creation);

        databaseEvent = FirebaseDatabase.getInstance().getReference("Event");

        eName = (EditText) findViewById(R.id.text_eName);
        eDes = (EditText) findViewById(R.id.text_eDes);
        eLocation = (EditText) findViewById(R.id.text_eLocation);
        sDate = (EditText) findViewById(R.id.text_sDate);
        eDate= (EditText) findViewById(R.id.text_eDate);
        sTime = (EditText) findViewById(R.id.text_sTime);
        eTime = (EditText) findViewById(R.id.text_eTime);
        Create = (Button) findViewById(R.id.btn_create);



        Create.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                addEvent();
            }
        });



    }

    public void addEvent(){
        String Name = eName.getText().toString().trim();
        String Des = eDes.getText().toString().trim();
        String Location = eLocation.getText().toString().trim();
        String STime = sTime.getText().toString().trim();
        String ETime = eTime.getText().toString().trim();
        String SDate = sDate.getText().toString().trim();
        String EDate = eDate.getText().toString().trim();

        fireUser = FirebaseAuth.getInstance().getCurrentUser();
        String User_ID = fireUser.getUid();
        boolean result = validate(Name,Des,Location,STime,ETime,SDate,EDate);
        if(result == true)
        {
           String eventid = databaseEvent.push().getKey();

            Event event = new Event(eventid,Name,Des,Location,SDate,EDate,STime,ETime,User_ID);

            databaseEvent.child(eventid).setValue(event);
            Toast.makeText(this,"Event added",Toast.LENGTH_LONG).show();
            startActivity(new Intent(EventCreation.this, EventView.class));
        }

    }
    public boolean validate(String Name, String Des, String Location, String STime, String ETime, String SDate, String EDate){

        if(TextUtils.isEmpty(Name)){
            Toast.makeText(this,"You should enter the Event Name", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(TextUtils.isEmpty(Des)){
            Toast.makeText(this,"You should enter the Event Description", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(TextUtils.isEmpty(Location)){
            Toast.makeText(this,"You should enter the Event Location", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(TextUtils.isEmpty(SDate)){
            Toast.makeText(this,"You should enter the Event Start Date", Toast.LENGTH_LONG).show();
            return false;

        }
        else if(TextUtils.isEmpty(EDate)){
            Toast.makeText(this,"You should enter the Event End Date", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(TextUtils.isEmpty(STime)){
            Toast.makeText(this,"You should enter the Event Start Time", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(TextUtils.isEmpty(ETime)){
            Toast.makeText(this,"You should enter the Event End Time", Toast.LENGTH_LONG).show();
            return false;
        }
        else
            return true;

    }
}
