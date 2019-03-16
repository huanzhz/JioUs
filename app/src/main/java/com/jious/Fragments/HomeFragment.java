
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
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jious.EventActivity.EventCreation;
import com.jious.EventActivity.EventDetailView;
import com.jious.EventActivity.EventList;
import com.jious.EventActivity.EventView;
import com.jious.Model.Event;
import com.jious.Model.Subscriber;
import com.jious.Model.User;
import com.jious.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {
    ListView listViewEvents;
    DatabaseReference databaseEvent,databaseSub,databaseUser;
    List<Event> eventList;
    List<Subscriber> subList;
    FirebaseUser fireUser;
    String User_ID;
    Button create;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        databaseEvent = FirebaseDatabase.getInstance().getReference("Event");
        databaseSub = FirebaseDatabase.getInstance().getReference("Subscriber");
        databaseUser = FirebaseDatabase.getInstance().getReference("Users");
        eventList = new ArrayList<>();
        subList = new ArrayList<>();


        return inflater.inflate(R.layout.fragment_home, null);
    }
    @Override
    public void onStart(){
        super.onStart();

        databaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fireUser = FirebaseAuth.getInstance().getCurrentUser();
                User_ID = fireUser.getUid();
                create = getActivity().findViewById(R.id.btn_createEvent);
                for(DataSnapshot user : dataSnapshot.getChildren()){
                    User cUser = user.getValue(User.class);
                    String userID = cUser.getId();
                    Long eCreator = cUser.geteCreator();
                    if(userID.equals(User_ID) && eCreator == 1){
                        create.setVisibility(View.VISIBLE);
                        create.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent i = new Intent(getActivity(), EventCreation.class);
                                startActivity(i);
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        databaseSub.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                subList.clear();
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
                       if(getActivity()!=null) {
                           EventList adapter = new EventList(getActivity(), eventList);
                           listViewEvents = (ListView) getActivity().findViewById(R.id.fragListViewEvent);
                           listViewEvents.setAdapter(adapter);
                           listViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                               @Override
                               public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                   String eventID;
                                   Event event = eventList.get(position);
                                   eventID = event.geteID();
                                   eventList.clear();
                                   Intent i = new Intent(getActivity(), EventDetailView.class);
                                   i.putExtra("EventID", eventID);
                                   startActivity(i);

                               }
                           });
                       }

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

