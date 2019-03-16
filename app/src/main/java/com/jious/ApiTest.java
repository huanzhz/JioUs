package com.jious;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class ApiTest extends AppCompatActivity {

    TextView api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_test);


         api = (TextView) findViewById(R.id.text_api);
        Button Call =(Button) findViewById(R.id.Call);
        Call.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

               TextView Datebox =(TextView) findViewById(R.id.DateBox);
               String Date = Datebox.getText().toString().trim();
                checkWeather(Date);
            }
        });




    }


    public void checkWeather(final String Date){
        String url = "https://api.apixu.com/v1/forecast.json?key=bc3127f6af2b4880a7d122952190803&q=Singapore&days=10";

      JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {
              try {
                  JSONObject outer = response.getJSONObject("forecast");

                    int check = 0;
                     JSONArray weather = outer.getJSONArray("forecastday");

                  for(int i=0;i< weather.length();i++){
                    JSONObject forecast = weather.getJSONObject(i);


                        JSONObject day = forecast.getJSONObject("day");
                        String checkDate = forecast.getString("date").trim();
                        if(checkDate.equals(Date)) {
                            JSONObject Condition = day.getJSONObject("condition");
                            String rain = Condition.getString("text").trim();

                            api.setText(rain);
                            if(rain.contains("rain"))
                            check =1;
                        }






                 }
                 if(check == 0)
                     api.setText(" ");



              }catch(JSONException e){
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

}
