package com.application.club.guestlist.bookedPasses;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.application.club.guestlist.R;
import com.application.club.guestlist.utils.UtillMethods;

import java.util.List;

public class BookedPassesListAdapter extends BaseAdapter {

	Context context;
	List<PassRowItem> rowItem;

	BookedPassesListAdapter(Context context, List<PassRowItem> rowItem) {
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
			convertView = mInflater.inflate(R.layout.table_booking_list_adapter, null);
		}


		PassRowItem passRowItem = rowItem.get(position);

		TextView clubName = (TextView) convertView.findViewById(R.id.clubName);
		clubName.setText(passRowItem.getClubname());
		TextView eventDate = (TextView) convertView.findViewById(R.id.eventDate);
		eventDate.setText(passRowItem.getEventDate());
		TextView details = (TextView) convertView.findViewById(R.id.description);
		details.setText(UtillMethods.toCamelCase(passRowItem.getTickettype()));
		//TextView passDetails = (TextView) convertView.findViewById(R.id.passDetails);


		// setting the image resource and title

		//txtTitle.setText(row_pos.getTitle());
//		ImageView naviIcon = (ImageView) convertView.findViewById(R.id.navi);
//
//		naviIcon.setOnClickListener(new View.OnClickListener() {
//			// Start new list activity
//			public void onClick(View v) {
//				//https://stackoverflow.com/questions/2662531/launching-google-maps-directions-via-an-intent-on-android
//				//http://android-pratap.blogspot.in/2014/09/launching-google-maps-directions-via.html
//				//https://gist.github.com/laaptu/3ad031740deb99b9bd60
//				Intent intent = new Intent(Intent.ACTION_VIEW,
//						Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345"));
//				context.startActivity(intent);
//			}
//		});

		return convertView;

	}


}
