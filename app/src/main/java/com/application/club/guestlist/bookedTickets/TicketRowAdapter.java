package com.application.club.guestlist.bookedTickets;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.club.guestlist.R;

import com.application.club.guestlist.utils.UtillMethods;

import java.util.List;

public class TicketRowAdapter extends BaseAdapter {

    Context context;
    List<TicketRowItem> rowItem;

    TicketRowAdapter(Context context, List<TicketRowItem> rowItem) {
        this.context = context;
        this.rowItem = rowItem;

    }

    @Override
    public int getCount() {

        return rowItem.size();
    }

    @Override
    public Object getItem(int position) {

        return rowItem.get(position);
    }

    @Override
    public long getItemId(int position) {

        return rowItem.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.ticket_list_item, null);
        }


        TicketRowItem ticketRowItem = rowItem.get(position);

        TextView clubName = (TextView) convertView.findViewById(R.id.qrNumber);
        clubName.setText(ticketRowItem.getQRnumber());
        TextView eventDate = (TextView) convertView.findViewById(R.id.eventDate);
        eventDate.setText(ticketRowItem.getEventDate());
        TextView details = (TextView) convertView.findViewById(R.id.description);
        details.setText(UtillMethods.toCamelCase(ticketRowItem.getTicketDetails()));
        //TextView passDetails = (TextView) convertView.findViewById(R.id.passDetails);




        return convertView;

    }

}

