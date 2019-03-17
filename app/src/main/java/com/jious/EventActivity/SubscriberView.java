package com.jious.EventActivity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import java.util.ArrayList;
import java.util.List;


public class SubscriberView extends AppCompatActivity {
    TextView tvSubName;
    FirebaseUser fireUser;
    ListView subEvent;
    Button sub;
    DatabaseReference databaseEvent,databaseSub, databaseUser;
    List<Event> eventList;
    List<Subscriber> subList;
    List<User> userList;
    String SID,User_ID,SubscriptionID;
    ImageView image_User;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber_view);
        tvSubName = (TextView) findViewById(R.id.tv_sub_name);
        subEvent = (ListView) findViewById(R.id.listview_sub_event);
        sub = (Button) findViewById(R.id.btn_sub);
        image_User = (ImageView) findViewById(R.id.sub_profile);
        fireUser = FirebaseAuth.getInstance().getCurrentUser();
        User_ID = fireUser.getUid();
        Intent i =this.getIntent();
        SID = i.getExtras().getString("SubscriberID");
        databaseEvent = FirebaseDatabase.getInstance().getReference("Event");
        databaseSub = FirebaseDatabase.getInstance().getReference("Subscriber");
        databaseUser = FirebaseDatabase.getInstance().getReference("Users");
        eventList = new ArrayList<>();
        subList = new ArrayList<>();
        userList = new ArrayList<>();

        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscribe();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseEvent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventList.clear();
                for(DataSnapshot eventSnapshot : dataSnapshot.getChildren()){
                    Event event = eventSnapshot.getValue(Event.class);
                    String eCreatorID = event.geteCreatorID();
                   if(eCreatorID.equals(SID)){
                        eventList.add(event);

                    }
                }

                EventListS adapter = new EventListS(SubscriberView.this, eventList);
                subEvent.setAdapter(adapter);
                subEvent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String eventID;
                        Event event = eventList.get(position);
                        eventID = event.geteID();
                        eventList.clear();
                        Intent i = new Intent(SubscriberView.this, EventDetailView.class);
                        i.putExtra("EventID",eventID);
                        startActivity(i);

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseSub.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                subList.clear();
                for(DataSnapshot subSnapshot : dataSnapshot.getChildren()){
                    Subscriber subscriber = subSnapshot.getValue(Subscriber.class);
                    String userID = subscriber.getUserID();
                    String subscriberID = subscriber.geteCreatorID();
                    if(userID.equals(User_ID) && subscriberID.equals(SID)){
                        sub.setText("Unsubscribe");
                        SubscriptionID = subscriber.getSubID();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for(DataSnapshot userSnapshot : dataSnapshot.getChildren() ){
                    User user = userSnapshot.getValue(User.class);
                    String userID = user.getId();
                    if(userID.equals(SID)){
                        String name = user.getUsername();
                        tvSubName.setText(name);
                        if(user.getImageURL().equals("default")){
                            image_User.setImageResource(R.mipmap.ic_launcher);
                        }
                        else{
                            Glide.with(SubscriberView.this).load(user.getImageURL()).into(image_User);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void subscribe(){
        String check = sub.getText().toString().trim();
        if (check.equals("Subscribe")) {
            String subID = databaseSub.push().getKey();
            Subscriber subscriber = new Subscriber(subID,SID,User_ID);
            databaseSub.child(subID).setValue(subscriber);
            sub.setText("Unsubscribe");
            Toast.makeText(this,"Subscribed",Toast.LENGTH_LONG).show();


        }
        else {
        databaseSub.child(SubscriptionID).removeValue();
        sub.setText("Subscribe");
            Toast.makeText(this,"Unsubscribed",Toast.LENGTH_LONG).show();

        }
    }
}
