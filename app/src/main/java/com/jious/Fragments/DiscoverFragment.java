package com.jious.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jious.EventActivity.SubscriberList;
import com.jious.EventActivity.SubscriberListView;
import com.jious.EventActivity.SubscriberView;
import com.jious.Model.User;
import com.jious.R;

import java.util.ArrayList;
import java.util.List;

public class DiscoverFragment extends Fragment {

    ListView listViewSubscriber;
    DatabaseReference databaseSubscriber;
    List<User> subscriberList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard

        databaseSubscriber = FirebaseDatabase.getInstance().getReference("Users");

        subscriberList = new ArrayList<>();
        return inflater.inflate(R.layout.fragment_discover, null);
    }
    @Override
    public void onStart() {
        super.onStart();

        databaseSubscriber.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listViewSubscriber = (ListView) getActivity().findViewById(R.id.fragListViewSubscribers);
                subscriberList.clear();
                for(DataSnapshot subscriberSnapshot : dataSnapshot.getChildren())
                {
                    final User user = subscriberSnapshot.getValue(User.class);
                    Long check = user.geteCreator();
                    if(check == 1){
                        subscriberList.add(user);
                    }

                }
                SubscriberList adapter = new SubscriberList(getActivity(), subscriberList);
                listViewSubscriber.setAdapter(adapter);

                listViewSubscriber.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                                                              @Override
                                                              public void onItemClick(AdapterView<?> adpaterView, View view, int position, long id){
                                                                  String subID;
                                                                  User sub = subscriberList.get(position);
                                                                  subID = sub.getId();
                                                                  Intent i = new Intent(getActivity(), SubscriberView.class);
                                                                  i.putExtra("SubscriberID",subID);
                                                                  startActivity(i);

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
