package com.application.club.guestlist.clubsListFragment;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.application.club.guestlist.R;
import com.application.club.guestlist.utils.Constants;
import com.application.club.guestlist.utils.UtillMethods;

public class ClubsListAdapter extends BaseAdapter {

	Context context;
	List<ClubRowItem> rowItem;



	ClubsListAdapter(Context context, List<ClubRowItem> rowItem) {
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
			convertView = mInflater.inflate(R.layout.club_list_adapter_item, null);
		}
        ClubRowItem clubRowItemObj = rowItem.get(position);
        String imgURL = Constants.HTTP_URL+clubRowItemObj.getImageURL();


        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.clubMainImage);
        Picasso.with(context).load(imgURL)
				//.networkPolicy(NetworkPolicy.OFFLINE)
				.into(imgIcon);
		TextView clubMainName = (TextView) convertView.findViewById(R.id.clubMainName);
        clubMainName.setText(UtillMethods.toCamelCase(clubRowItemObj.getClubname()));
        TextView location = (TextView) convertView.findViewById(R.id.location);
        location.setText(UtillMethods.toCamelCase(clubRowItemObj.getLocation()));

        final String latLong = clubRowItemObj.getLatlong();

		ImageView naviIcon = (ImageView) convertView.findViewById(R.id.navi);

		naviIcon.setOnClickListener(new View.OnClickListener() {
			// Start new list activity
			public void onClick(View v) {
				//https://stackoverflow.com/questions/2662531/launching-google-maps-directions-via-an-intent-on-android
				//http://android-pratap.blogspot.in/2014/09/launching-google-maps-directions-via.html
				//https://gist.github.com/laaptu/3ad031740deb99b9bd60


                //https://stackoverflow.com/questions/2662531/launching-google-maps-directions-via-an-intent-on-android
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("https")
                        .authority("www.google.com").appendPath("maps").appendPath("dir").appendPath("").appendQueryParameter("api", "1")
                        .appendQueryParameter("destination", latLong);
                String url = builder.build().toString();
                Log.d("Directions", url);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
			}
		});

		return convertView;

	}

}
