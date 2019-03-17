package com.jious.EventActivity;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jious.Fragments.ProfileFragment;
import com.jious.Model.Subscriber;
import com.jious.Model.User;
import com.jious.R;

import java.util.List;

public class SubscriberList extends ArrayAdapter<User> {

    FirebaseUser fireUser;
    private DatabaseReference databaseSubscribe;
    ImageView image_User;
    private Activity context;
    private List<User> subscriberList;

    public SubscriberList(Activity context, List<User> subscriberList){

        super(context, R.layout.subscriber_list_layout,subscriberList);
        this.context = context;
        this.subscriberList = subscriberList;


    }

    @NonNull
    @Override
    public View getView(int position,View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.subscriber_list_layout,null,true);


        TextView eCreatorName = (TextView) listViewItem.findViewById(R.id.tv_eCreatorName);

        User subscriber = subscriberList.get(position);
        eCreatorName.setText(subscriber.getUsername());
        image_User = (ImageView) listViewItem.findViewById(R.id.imageView_Subscriber);
        if(subscriber.getImageURL().equals("default")){
            image_User.setImageResource(R.mipmap.ic_launcher);
        }
        else{
            Glide.with(listViewItem).load(subscriber.getImageURL()).into(image_User);


        }



        return listViewItem;

    }







}
