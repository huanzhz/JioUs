package com.jious.EventActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventCreation extends AppCompatActivity {

    EditText eName,eDes,eLocation,sDate,sTime,eDate,eTime;

    Button Create,Weather;
    Switch Visibility;


     FirebaseUser fireUser;
    private DatabaseReference databaseEvent, RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_creation1);

        databaseEvent = FirebaseDatabase.getInstance().getReference("Event");
        RootRef = FirebaseDatabase.getInstance().getReference();

        eName = (EditText) findViewById(R.id.text_eName);
        eDes = (EditText) findViewById(R.id.text_eDes);
        eLocation = (EditText) findViewById(R.id.text_eLocation);
        sDate = (EditText) findViewById(R.id.text_sDate);
        eDate= (EditText) findViewById(R.id.text_eDate);
        sTime = (EditText) findViewById(R.id.text_sTime);
        eTime = (EditText) findViewById(R.id.text_eTime);

        Create = (Button) findViewById(R.id.btn_create);
        Weather = (Button) findViewById(R.id.btn_weather);
        Visibility = (Switch) findViewById(R.id.switch_vis);



        Create.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                addEvent();
            }
        });
        Weather.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String SDate = sDate.getText().toString().trim();
                String EDate = eDate.getText().toString().trim();
                boolean result = checkDate(SDate,EDate);
                if(result == true) {
                    Intent i = new Intent(EventCreation.this, WeatherCheck.class);
                    i.putExtra("SDate", SDate);
                    i.putExtra("EDate", EDate);
                    startActivity(i);
                }

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
        String Vis;
        if (Visibility.isChecked()){
            Vis = Visibility.getTextOn().toString();
        }
        else
            Vis = Visibility.getTextOff().toString();

        fireUser = FirebaseAuth.getInstance().getCurrentUser();
        String User_ID = fireUser.getUid();
        boolean result = validate(Name,Des,Location,STime,ETime,SDate,EDate);
        if(result == true)
        {
           String eventid = databaseEvent.push().getKey();

            Event event = new Event(eventid,Name,Des,Location,SDate,EDate,STime,ETime,User_ID,Vis,"default");

            databaseEvent.child(eventid).setValue(event);
            Toast.makeText(this,"Event added",Toast.LENGTH_LONG).show();
            Intent i = new Intent(EventCreation.this, EventDetailView.class);
            i.putExtra("EventID",eventid);
            startActivity(i);


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

        else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            Date startDate,endDate;
            try {
                startDate = dateFormat.parse(SDate);
                endDate = dateFormat.parse(EDate);
            } catch (ParseException e) {
                Toast.makeText(this,"You should enter a valid Date", Toast.LENGTH_LONG).show();
                return false;
            }
            try{
                timeFormat.parse(STime);
                timeFormat.parse(ETime);
            } catch (ParseException e){
                Toast.makeText(this, "You should enter a valid Time", Toast.LENGTH_LONG).show();
                return false;
            }
            boolean results = checkDate(SDate,EDate);
            if(results == false){
                return false;
            }
            else{
                return true;
            }

        }

    }


    public boolean checkDate(String StDate, String EdDate) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate,endDate;
        try {
            startDate = dateFormat.parse(StDate);
            endDate = dateFormat.parse(EdDate);
        } catch (ParseException e) {
            Toast.makeText(this,"You should enter a valid Date", Toast.LENGTH_LONG).show();
            return false;
        }
        if(StDate.matches("^\\d{4}-\\d{2}-\\d{2}$") && EdDate.matches("^\\d{4}-\\d{2}-\\d{2}$"))
            if (startDate.compareTo(endDate) <= 0)
                return true;
            else {
                Toast.makeText(this, "End Date is before Start Date", Toast.LENGTH_LONG).show();
                return false;
            }
        else
            Toast.makeText(this,"You should enter a valid Date", Toast.LENGTH_LONG).show();
        return false;
    }





}
