package com.jious.EventActivity;

import android.app.Activity;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jious.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeatherCheck extends Activity {

    TextView w1,w2,w3,d1,d2,d3;
    @Override
    protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.popup_weather);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        Intent i =this.getIntent();
        String StartDate = i.getExtras().getString("SDate");
        String EndDate =i.getExtras().getString("EDate");
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        w1 = (TextView) findViewById(R.id.tv_w1);
        w2 = (TextView) findViewById(R.id.tv_w2);
        w3 = (TextView) findViewById(R.id.tv_w3);
        d1 = (TextView) findViewById(R.id.tv_d1);
        d2 = (TextView) findViewById(R.id.tv_d2);
        d3 = (TextView) findViewById(R.id.tv_d3);

        WeatherCheck(StartDate,EndDate);
        getWindow().setLayout((int)(width*.65),(int)(height*.5));
    }



    public void WeatherCheck(final String StDate, final String EdDate){

        //clear Text
        w1.setText("");
        w2.setText("");
        w3.setText("");
        d1.setText("");
        d2.setText("");
        d3.setText("");
        //end clear Text


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
                        w1.setText(Result);

                    }
                    else if(rainCheck == 0) {
                        Result = "Weather forecast shows no chance of rain";
                        w1.setText(Result);
                    }
                    else {
                        String r ="";
                        w1.setText("Dates with chance of rain:");
                        for(Date date:rainDates){

                            r += df1.format(date) +" \n";
                        }
                        d1.setText(r);
                        if(clearNum != 0) {
                            w2.setText("Dates without any chance of rain:");
                            r ="";
                            for (Date date : clearDates) {
                                r += df1.format(date) + " \n";
                            }
                            d2.setText(r);
                        }
                        List<Date> unusedDates = removeAppearedDate(dates,rainDates,clearDates);
                        if(unusedDates.size() !=0){
                            w3.setText("Dates too far ahead to forecast:");
                            r ="";
                            for(Date date : unusedDates){
                                r += df1.format(date) + " \n";
                            }
                            d3.setText(r);
                        }


                    }



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

