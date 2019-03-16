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
    TextView tvWeather;
    Button Create,Weather;
    Switch Visibility;


     FirebaseUser fireUser;
    private DatabaseReference databaseEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_creation1);

        databaseEvent = FirebaseDatabase.getInstance().getReference("Event");

        eName = (EditText) findViewById(R.id.text_eName);
        eDes = (EditText) findViewById(R.id.text_eDes);
        eLocation = (EditText) findViewById(R.id.text_eLocation);
        sDate = (EditText) findViewById(R.id.text_sDate);
        eDate= (EditText) findViewById(R.id.text_eDate);
        sTime = (EditText) findViewById(R.id.text_sTime);
        eTime = (EditText) findViewById(R.id.text_eTime);
        tvWeather= (TextView) findViewById(R.id.textViewWeather);
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

                WeatherCheck(SDate,EDate);
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

            Event event = new Event(eventid,Name,Des,Location,SDate,EDate,STime,ETime,User_ID,Vis);

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


    public void WeatherCheck(final String StDate, final String EdDate){




        String url = "https://api.apixu.com/v1/forecast.json?key=bc3127f6af2b4880a7d122952190803&q=Singapore&days=10";

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    List<Date> dates = getDates(StDate,EdDate);
                    ArrayList<Date> rainDates = new ArrayList<>();
                    ArrayList<Date> clearDates = new ArrayList<>();
                    DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
                    String Result ="a";
                    int rainCheck,dateCheck,clearNum;

                    rainCheck = 0; dateCheck=0; clearNum=0;

                    JSONObject outer = response.getJSONObject("forecast");
                    JSONArray weather = outer.getJSONArray("forecastday");

                    for(int i=0;i< weather.length();i++){
                        JSONObject forecast = weather.getJSONObject(i);
                        JSONObject day = forecast.getJSONObject("day");
                        String checkDate = forecast.getString("date").trim();
                        for(Date date:dates) {
                           String varDate = df1.format(date);

                           if(varDate.equals(checkDate)) {
                               JSONObject Condition = day.getJSONObject("condition");
                               String rain = Condition.getString("text").trim();
                                    dateCheck=1;

                               if (rain.contains("rain")) {
                                   rainCheck = 1;
                                   Date date1 = df1.parse(checkDate);
                                   Calendar cal1 = Calendar.getInstance();
                                   cal1.setTime(date1);
                                   rainDates.add(cal1.getTime());



                               }
                               else{
                                   Date date1= df1.parse(checkDate);
                                   Calendar cal1 = Calendar.getInstance();
                                   cal1.setTime(date1);
                                   clearDates.add(cal1.getTime());
                                   clearNum++;

                               }


                           }
                        }
                    }
                    if(dateCheck == 0){
                        Result = "Unable to forecast weather as date is too far ahead";
                    }
                    else if(rainCheck == 0) {
                        Result = "Weather forecast shows no chance of rain";
                    }
                    else {
                        String r ="";
                        r += "Dates with chance of rain: \n";
                        for(Date date:rainDates){

                            r += df1.format(date) +" \n";
                        }
                        if(clearNum != 0) {
                            r += " Dates without any chance of rain: \n";
                            for (Date date : clearDates) {
                                r += df1.format(date) + " \n";
                            }
                        }
                        List<Date> unusedDates = removeAppearedDate(dates,rainDates,clearDates);
                        if(unusedDates.size() !=0){
                            r += "Dates too far ahead to forecast: \n";
                            for(Date date : unusedDates){
                                r += df1.format(date) + " \n";
                            }
                        }

                        Result = r;
                    }
                    tvWeather.setText(Result);


                }catch(JSONException e){
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jor);
     }



     public static List<Date> getDates(String dateString1, String dateString2){
        ArrayList<Date> dates = new ArrayList<>();
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        Date date2 = null;

        try{
            date1 = df1.parse(dateString1);
            date2 = df1.parse(dateString2);

        }catch(ParseException e){
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        while(!cal1.after(cal2)){
            dates.add(cal1.getTime());
            cal1.add(Calendar.DATE,1);
        }
        return dates;
     }
     public static List<Date> removeAppearedDate(List<Date> o,List<Date> rain, List<Date> clear) throws ParseException {
         ArrayList<Date> dates = new ArrayList<>();
         int check = 0;
        for(Date date:o){
             check = 0;

            for(Date rainDate:rain){

                if(date.equals(rainDate)){
                    check = 1;
                }

            }

            for(Date clearDate:clear){

                if(date.equals(clearDate)){
                  check = 1;
                }

            }
            if(check == 0)
            {
                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(date);
                dates.add(cal1.getTime());
            }
        }

        return dates;

     }


}
