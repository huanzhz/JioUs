package com.jious.EventActivity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jious.Model.Event;
import com.jious.Model.Subscriber;
import com.jious.Model.User;
import com.jious.R;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SubscriptionEventView extends AppCompatActivity {


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
        setContentView(R.layout.activity_subscription_event_view);
        listViewEvents = (ListView) findViewById(R.id.listViewEventS);
        fireUser = FirebaseAuth.getInstance().getCurrentUser();
        User_ID = fireUser.getUid();
        eventList = new ArrayList<>();
        subList = new ArrayList<>();



    }

    @Override
    protected void onStart(){
        super.onStart();

        databaseSub.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                subList.clear();
                for(DataSnapshot subSnapshot : dataSnapshot.getChildren()){
                    Subscriber subscriber = subSnapshot.getValue(Subscriber.class);
                    String userID = subscriber.getUserID();
                    //check if it is the user that has subscribed
                    if(User_ID.equals(userID)) {

                        subList.add(subscriber);
                    }
                }

                databaseEvent.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        eventList.clear();

                        for(DataSnapshot eventSnapshot : dataSnapshot.getChildren()){
                            Event event = eventSnapshot.getValue(Event.class);
                            String viewAccess = event.getviewAccess();
                            String eCreatorID = event.geteCreatorID();

                            int size = subList.size();

                            for(int i =0; i<size; i++) {
                                Subscriber sub = subList.get(i);
                                String check = sub.geteCreatorID();
                                //Checking if the event is made by subscriber
                                if (eCreatorID.equals(check)) {
                                    if (viewAccess.equals("Subscribed")) {

                                        eventList.add(event);

                                    }
                                }
                            }

                        }
                        EventList adapter = new EventList(SubscriptionEventView.this, eventList);
                        listViewEvents.setAdapter(adapter);
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