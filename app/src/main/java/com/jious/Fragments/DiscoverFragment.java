package com.jious.Fragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jious.EventActivity.SubscriberList;
import com.jious.EventActivity.SubscriberView;
import com.jious.Model.User;
import com.jious.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DiscoverFragment extends Fragment {

    RecyclerView rvEvents;
    ArrayList<Uri> mImageUris = new ArrayList<>();
    ArrayList<String> mNames = new ArrayList<>();

    ListView listViewSubscriber;
    DatabaseReference databaseSubscriber;
    List<User> subscriberList;

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        public RecyclerViewAdapter(Context mContext, ArrayList<Uri> mEventImageUris, ArrayList<String> mEventNames) {
            this.mContext = mContext;
            this.mEventImageUris = mEventImageUris;
            this.mEventNames = mEventNames;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater
                    .from(viewGroup.getContext())
                    .inflate(R.layout.listitem_discover, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            Glide.with(mContext)
                    .asBitmap()
                    .load(mEventImageUris.get(i))
                    .into(viewHolder.civ_event_image);

            viewHolder.tv_event_name.setText(mEventNames.get(i));
        }

        @Override
        public int getItemCount() {
            return mEventNames.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            CircleImageView civ_event_image;
            TextView tv_event_name;


            ViewHolder(@NonNull View itemView) {
                super(itemView);

                civ_event_image = itemView.findViewById(R.id.civ_event_image);
                tv_event_name = itemView.findViewById(R.id.tv_event_name);
            }
        }

        private Context mContext;
        private ArrayList<Uri> mEventImageUris;
        private ArrayList<String> mEventNames;
    }

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvEvents = getActivity().findViewById(R.id.rv_events);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), mImageUris, mNames);
        rvEvents.setAdapter(adapter);
        rvEvents.setLayoutManager(new LinearLayoutManager(getContext()));

        Uri imageUri = Uri.parse("android.resource://com.jious/drawable/ic_launcher_background");

        //TODO populate discover data from firebase
        for (int i = 0; i < 30; i++) {
            mImageUris.add(imageUri);
            mNames.add("test");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        /*
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
        */



    }

}
