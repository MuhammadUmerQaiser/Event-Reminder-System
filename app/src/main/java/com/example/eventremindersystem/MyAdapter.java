package com.example.eventremindersystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class MyAdapter extends ArrayAdapter<EventListing> {
    Context context;
    List<EventListing> arrayEventListing;

    public MyAdapter(@NonNull Context context, List<EventListing> arrayEventListing){
        super(context, R.layout.custom_event_list, arrayEventListing);
        this.context = context;
        this.arrayEventListing = arrayEventListing;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_event_list, null, true);

        TextView event_id = view.findViewById(R.id.event_id);
        TextView event_name = view.findViewById(R.id.event_name);
        TextView event_time = view.findViewById(R.id.event_time);
        TextView event_date = view.findViewById(R.id.event_date);

        event_id.setText(arrayEventListing.get(position).getId());
        event_name.setText(arrayEventListing.get(position).getName());
        event_date.setText(arrayEventListing.get(position).getDate());
        event_time.setText(arrayEventListing.get(position).getTime());

        return view;
    }
}
