package com.jious.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jious.EventActivity.SubscriberList;
import com.jious.EventActivity.SubscriberView;
import com.jious.GroupChatActivity;
import com.jious.Model.Subscriber;
import com.jious.Model.User;
import com.jious.R;

import java.util.ArrayList;
import java.util.List;

public class GroupChatFragment extends Fragment {

    private FirebaseUser fireUser;
    private DatabaseReference userRef,RootRef, GroupRef, subscrRef;
    private List<User> subscriberList;
    private ListView listViewSubscriber;

    private List<Subscriber> subList;
    private List<User> userList;
    String User_ID;

    public GroupChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        userRef = FirebaseDatabase.getInstance().getReference("Users");
        RootRef = FirebaseDatabase.getInstance().getReference();
        GroupRef = FirebaseDatabase.getInstance().getReference("Groups");

        subscrRef = FirebaseDatabase.getInstance().getReference("Subscriber");
        subList = new ArrayList<>();
        userList = new ArrayList<>();

        subscriberList = new ArrayList<>();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_chat, null);
    }

//    @Override
////    public void onStart() {
////        super.onStart();
////
////        userRef.addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                listViewSubscriber = (ListView) getActivity().findViewById(R.id.fragGroupChat);
////                subscriberList.clear();
////                for(DataSnapshot subscriberSnapshot : dataSnapshot.getChildren())
////                {
////                    final User user = subscriberSnapshot.getValue(User.class);
////                    Long check = user.geteCreator();
////                    if(check == 1){
////                        subscriberList.add(user);
////                    }
////
////                }
////                SubscriberList adapter = new SubscriberList(getActivity(), subscriberList);
////                listViewSubscriber.setAdapter(adapter);
////
////                listViewSubscriber.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////                      @Override
////                      public void onItemClick(AdapterView<?> adpaterView, View view, int position, long id) {
////                          String userID;
////                          User sub = subscriberList.get(position);
////                          userID = sub.getUsername();
////                          Intent i = new Intent(getActivity(), GroupChatActivity.class);
////                          i.putExtra("groupName", userID);
////                          startActivity(i);
////
////                      }
////                  }
////
////                );
////
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError databaseError) {
////
////            }
////        });
////    }

    @Override
    public void onStart() {
        super.onStart();
        subscrRef.addValueEventListener(new ValueEventListener() {
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
                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userList.clear();
                        for(DataSnapshot Sub : dataSnapshot.getChildren()){
                            User user = Sub.getValue(User.class);
                            int size, i;
                            String userID = user.getId();
                            size = subList.size();
                            for(i = 0; i<size; i++){
                                Subscriber subC = subList.get(i);
                                String check = subC.geteCreatorID();
                                if(userID.equals(check)){
                                    userList.add(user);
                                }
                            }


                        }
                        if(getActivity()!=null) {
                            listViewSubscriber = (ListView) getActivity().findViewById(R.id.fragGroupChat);
                            SubscriberList adapter = new SubscriberList(getActivity(), userList);
                            listViewSubscriber.setAdapter(adapter);

                            listViewSubscriber.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adpaterView, View view, int position, long id) {
                                    String userID;
                                      User sub = userList.get(position);
                                      userID = sub.getUsername();
                                    Intent i = new Intent(getActivity(), GroupChatActivity.class);
                                    i.putExtra("groupName", userID);
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
