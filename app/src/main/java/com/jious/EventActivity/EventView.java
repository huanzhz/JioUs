package com.jious.EventActivity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jious.Model.Event;
import com.jious.Model.Subscriber;
import com.jious.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

public class EventView extends AppCompatActivity {

    ListView listViewEvents;
    DatabaseReference databaseEvent,databaseSub;
    List<Event> eventList;
    List<Subscriber> subList;
    FirebaseUser fireUser;
    String User_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        databaseEvent = FirebaseDatabase.getInstance().getReference("Event");
        databaseSub = FirebaseDatabase.getInstance().getReference("Subscriber");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);
        listViewEvents = (ListView) findViewById(R.id.listViewEvent);
        eventList = new ArrayList<>();
        subList = new ArrayList<>();

    }

    @Override
    protected void onStart(){
        super.onStart();

        databaseSub.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot sub : dataSnapshot.getChildren()){
                    Subscriber subscriber = sub.getValue(Subscriber.class);
                    String userID = subscriber.getUserID();
                    fireUser = FirebaseAuth.getInstance().getCurrentUser();
                    User_ID = fireUser.getUid();
                    if(userID.equals(User_ID)){
                        subList.add(subscriber);
                    }

                }

                databaseEvent.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        eventList.clear();

                        for(DataSnapshot eventSnapshot : dataSnapshot.getChildren()){
                            final Event event = eventSnapshot.getValue(Event.class);
                            String SDate = event.getsDate();
                            String EDate = event.geteDate();
                            final String eCreatorID = event.geteCreatorID();
                            String viewAccess = event.getviewAccess();
                            fireUser = FirebaseAuth.getInstance().getCurrentUser();
                            User_ID = fireUser.getUid();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Date Sdate = null, Edate = null;
                            try {
                                Sdate = sdf.parse(SDate);
                                Edate = sdf.parse(EDate);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            //Condition to ensure that event is meant for public instead of subscribed
                            if(viewAccess.equals("Public")) {
                                //Condition to ensure that event has not pass current date
                                if (System.currentTimeMillis() > Sdate.getTime()) {
                                    if (System.currentTimeMillis() <= Edate.getTime())
                                        eventList.add(event);
                                }
                                    else
                                        eventList.add(event);
                            }
                            else {
                                int size = subList.size();
                                int i;
                                for(i = 0; i < size; i ++){
                                    Subscriber sub = subList.get(i);
                                    String check = sub.geteCreatorID();
                                    String sDate = event.getsDate();
                                    String eDate = event.geteDate();
                                    SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
                                    Date sdate = null, edate = null;
                                    try {
                                        sdate = SDF.parse(SDate);
                                        edate = SDF.parse(EDate);

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    if(event.geteCreatorID().equals(check)){
                                        if (System.currentTimeMillis() > Sdate.getTime()) {
                                            if (System.currentTimeMillis() <= Edate.getTime())
                                                eventList.add(event);
                                        }
                                        else
                                            eventList.add(event);
                                    }

                                }


                            }

                        }

                        EventList adapter = new EventList(EventView.this, eventList);
                        listViewEvents.setAdapter(adapter);
                        listViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String eventID;
                                Event event = eventList.get(position);
                                eventID = event.geteID();
                                Intent i = new Intent(EventView.this,EventDetailView.class);
                                i.putExtra("EventID",eventID);
                                startActivity(i);
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
