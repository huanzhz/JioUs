package com.jious.EventActivity;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jious.Model.Event;
import com.jious.R;

import java.util.List;

public class EventListS extends ArrayAdapter<Event> {
    private Activity context;
    private List<Event> eventList;

    public EventListS(Activity context,List<Event> eventList){

        super(context, R.layout.listitem_subscriberevent,eventList);
        this.context = context;
        this.eventList = eventList;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.listitem_subscriberevent,null,true);

        TextView textName = (TextView) listViewItem.findViewById(R.id.textNameS);
        // TextView textLocation = (TextView) listViewItem.findViewById(R.id.textLocation);
        TextView textSDate = (TextView) listViewItem.findViewById(R.id.textSDateS);
        TextView textEDate = (TextView) listViewItem.findViewById(R.id.textEDateS);
        TextView textSTime = (TextView) listViewItem.findViewById(R.id.textSTimeS);
        TextView textETime = (TextView) listViewItem.findViewById(R.id.textETimeS);

        Event event = eventList.get(position);

        textName.setText(event.geteName());
        //  textLocation.setText(event.geteLocation());
        textSDate.setText(event.getsDate());
        textEDate.setText(event.geteDate());
        textSTime.setText(event.getsTime());
        textETime.setText("to "+event.geteTime());

        return listViewItem;

    }
}
