package com.jious.EventActivity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jious.Model.Event;
import com.jious.R;

public class EventDetailView extends AppCompatActivity {

    TextView eName,eDes,eLocation,eDate,eTime;
    DatabaseReference databaseEvent;
    String EventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail_view);
        Intent i = this.getIntent();
        EventID = i.getExtras().getString("EventID");
        eName = (TextView) findViewById(R.id.tv_eventName);
        eDes = (TextView) findViewById(R.id.tv_eventDesc);
        eLocation = (TextView) findViewById(R.id.tv_eventVenue);
        eDate = (TextView) findViewById(R.id.tv_eventDate);
        eTime = (TextView) findViewById(R.id.tv_eventTime);
        databaseEvent = FirebaseDatabase.getInstance().getReference("Event");


    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseEvent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot eventSS: dataSnapshot.getChildren()){
                    Event event = eventSS.getValue(Event.class);
                    String check = event.geteID();
                    if(EventID.equals(check)){
                        eName.setText("Name ; "+event.geteName());
                        eDes.setText("Description : "+ event.geteDes());
                        eLocation.setText("Venue : " + event.geteLocation());
                        eDate.setText("Date : " +event.getsDate() + " - " + event.geteDate());
                        eTime.setText(("Time : " + event.getsTime() + " - " + event.geteTime()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
