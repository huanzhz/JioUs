package com.jious.EventActivity;
import com.jious.Model.Event;
import com.jious.R;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.List;

public class EventList extends ArrayAdapter<Event> {

    private Activity context;
    private List<Event> eventList;

    public EventList(Activity context,List<Event> eventList){

        super(context, R.layout.event_list_layout,eventList);
        this.context = context;
        this.eventList = eventList;

    }

    @NonNull
    @Override
    public View getView(int position,View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.event_list_layout,null,true);

        TextView textName = (TextView) listViewItem.findViewById(R.id.textName);
       // TextView textLocation = (TextView) listViewItem.findViewById(R.id.textLocation);
        TextView textSDate = (TextView) listViewItem.findViewById(R.id.textSDate);
        TextView textEDate = (TextView) listViewItem.findViewById(R.id.textEDate);
        //TextView textSTime = (TextView) listViewItem.findViewById(R.id.textSTime);
       // TextView textETime = (TextView) listViewItem.findViewById(R.id.textETime);

        Event event = eventList.get(position);

        textName.setText(event.geteName());
      //  textLocation.setText(event.geteLocation());
        textSDate.setText(event.getsDate());
        textEDate.setText(event.geteDate());
       // textSTime.setText(event.getsTime());
       // textETime.setText("to "+event.geteTime());

        return listViewItem;

    }
}
