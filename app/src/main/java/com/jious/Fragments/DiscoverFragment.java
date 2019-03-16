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
import android.widget.Toast;

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

    RecyclerView rvUsers;
    ArrayList<Uri> mImageUris = new ArrayList<>();
    ArrayList<String> mNames = new ArrayList<>();

    ListView listViewSubscriber;
    DatabaseReference databaseSubscriber;
    List<User> subscriberList;

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        public RecyclerViewAdapter(Context mContext, ArrayList<Uri> mUserImageUris, ArrayList<String> mUserNames) {
            this.mContext = mContext;
            this.mUserImageUris = mUserImageUris;
            this.mUserNames = mUserNames;
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
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
            Glide.with(mContext)
                    .asBitmap()
                    .load(mUserImageUris.get(i))
                    .into(viewHolder.civ_user_image);

            viewHolder.tv_user_name.setText(mUserNames.get(i));

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO Implement on click listeners here
                    String subID;
                    User sub = subscriberList.get(viewHolder.getAdapterPosition());
                    subID = sub.getId();
                    Intent i = new Intent(getActivity(), SubscriberView.class);
                    i.putExtra("SubscriberID",subID);
                    startActivity(i);

                }
            });
        }

        @Override
        public int getItemCount() {
            return mUserNames.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            CircleImageView civ_user_image;
            TextView tv_user_name;


            ViewHolder(@NonNull View itemView) {
                super(itemView);

                civ_user_image = itemView.findViewById(R.id.civ_user_image);
                tv_user_name = itemView.findViewById(R.id.tv_user_name);
            }
        }

        private Context mContext;
        private ArrayList<Uri> mUserImageUris;
        private ArrayList<String> mUserNames;
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
    public void onStart() {
        super.onStart();

        databaseSubscriber.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //listViewSubscriber = (ListView) getActivity().findViewById(R.id.fragListViewSubscribers);
                subscriberList.clear();
                mNames.clear();
                mImageUris.clear();
                for(DataSnapshot subscriberSnapshot : dataSnapshot.getChildren())
                {
                    final User user = subscriberSnapshot.getValue(User.class);
                    Long check = user.geteCreator();
                    if(check == 1){
                        subscriberList.add(user);
                    }

                }
                rvUsers = getActivity().findViewById(R.id.rv_users);
                RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), mImageUris, mNames);
                rvUsers.setAdapter(adapter);
                rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));

                Uri imageUri = Uri.parse("android.resource://com.jious/drawable/ic_launcher_background");

                //TODO populate discover data from firebase here (or from onStart)
                int size = subscriberList.size();
                for (int i = 0; i < size; i++) {
                    User user = subscriberList.get(i);

                    if(user.getImageURL().equals("default"))
                    mImageUris.add(imageUri);
                    else
                        mImageUris.add(Uri.parse(user.getImageURL()));
                    mNames.add(user.getUsername());
                }
                /*
               SubscriberList adapter = new SubscriberList(getActivity(), subscriberList);
                listViewSubscriber.setAdapter(adapter);

                listViewSubscriber.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){
                    String subID;
                    User sub = subscriberList.get(position);
                    subID = sub.getId();
                    Intent i = new Intent(getActivity(), SubscriberView.class);
                    i.putExtra("SubscriberID",subID);
                    startActivity(i);

                     }
                });*/

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

}
