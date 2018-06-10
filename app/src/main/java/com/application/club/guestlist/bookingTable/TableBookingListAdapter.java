package com.application.club.guestlist.bookingTable;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.application.club.guestlist.R;
import com.application.club.guestlist.clubdetails.TicketDetailsItem;

import java.util.ArrayList;
import java.util.List;

public class TableBookingListAdapter extends ArrayAdapter<TicketDetailsItem> {

	Context context;
	List<TicketDetailsItem> rowItem;

	TableBookingListAdapter(Context context, ArrayList<TicketDetailsItem> rowItem) {
		super(context, 0, rowItem);
		this.context = context;
		this.rowItem = rowItem;

	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.table_booking_list_adapter, null);
		}

		TicketDetailsItem ticketDetailsItemObj = getItem(position);


//		ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
//		imgIcon.setScaleType(ImageView.ScaleType.FIT_XY);

		TextView clubNametv = (TextView) convertView.findViewById(R.id.clubName);
		clubNametv.setText(ticketDetailsItemObj.getClubname());

		TextView descriptiontv = (TextView) convertView.findViewById(R.id.description);
		descriptiontv.setText("Table for "+ticketDetailsItemObj.getSize());

		TextView eventDatetv = (TextView) convertView.findViewById(R.id.eventDate);
		eventDatetv.setText(ticketDetailsItemObj.getDate());



//		TextView tableSize = (TextView) convertView.findViewById(R.id.tableSize);
//        tableSize.setText("Table for "+ticketDetailsItemObj.getSize());
//		TextView date = (TextView) convertView.findViewById(R.id.date);
//        date.setText(ticketDetailsItemObj.getDate());
//		TextView tableCosttv = (TextView) convertView.findViewById(R.id.tableCost);
//        tableCosttv.setText(ticketDetailsItemObj.getCost()+" Rs");

		TicketDetailsItem row_pos = rowItem.get(position);


		return convertView;

	}


}
