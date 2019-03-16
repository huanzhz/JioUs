package com.jious.EventActivity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jious.Model.Event;
import com.jious.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

public class EventView extends AppCompatActivity {

    ListView listViewEvents;
    DatabaseReference databaseEvent;
    List<Event> eventList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        databaseEvent = FirebaseDatabase.getInstance().getReference("Event");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);
        listViewEvents = (ListView) findViewById(R.id.listViewEvent);
        eventList = new ArrayList<>();

    }

    @Override
    protected void onStart(){
        super.onStart();

        databaseEvent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                eventList.clear();

                for(DataSnapshot eventSnapshot : dataSnapshot.getChildren()){
                    Event event = eventSnapshot.getValue(Event.class);
                    String SDate = event.getsDate();
                    String EDate = event.geteDate();
                    String viewAccess = event.getviewAccess();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date Sdate = null, Edate = null;
                    try {
                         Sdate = sdf.parse(SDate);
                         Edate = sdf.parse(EDate);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //Condition to ensure that event is meant for public instead of subscribed
                        if(viewAccess == "Public") {
                            //Condition to ensure that event has not pass current date
                            if (System.currentTimeMillis() > Sdate.getTime())
                                if (System.currentTimeMillis() <= Edate.getTime())
                                    eventList.add(event);
                                else
                                    eventList.add(event);
                        }

                }
                EventList adapter = new EventList(EventView.this, eventList);
                listViewEvents.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
