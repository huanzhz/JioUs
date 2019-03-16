package com.jious.EventActivity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jious.MainActivity;
import com.jious.Model.Subscriber;
import com.jious.Model.User;
import com.jious.R;

import java.util.ArrayList;
import java.util.List;

public class SubscriberListView extends AppCompatActivity {


    ListView listViewSubscriber;
    DatabaseReference databaseSubscriber;
    List<User> subscriberList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber_list_view);
        databaseSubscriber = FirebaseDatabase.getInstance().getReference("Users");
        listViewSubscriber = (ListView) findViewById(R.id.listviewSubscriberList);
        subscriberList = new ArrayList<>();


    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseSubscriber.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                subscriberList.clear();
                for(DataSnapshot subscriberSnapshot : dataSnapshot.getChildren())
                {
                    final User user = subscriberSnapshot.getValue(User.class);
                    Long check = user.geteCreator();
                        if(check == 1){
                            subscriberList.add(user);
                       }

                }
                 SubscriberList adapter = new SubscriberList(SubscriberListView.this, subscriberList);
                listViewSubscriber.setAdapter(adapter);

                listViewSubscriber.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adpaterView, View view, int position, long id){
                        String subID;
                        User sub = subscriberList.get(position);
                        subID = sub.getId();
                        Intent i = new Intent(SubscriberListView.this, SubscriberView.class);
                        i.putExtra("SubscriberID",subID);
                        startActivity(i);
                        finish();
                    }
                                                          }

                );

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


}
