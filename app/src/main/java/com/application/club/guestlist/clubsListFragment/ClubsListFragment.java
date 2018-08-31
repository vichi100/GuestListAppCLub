package com.application.club.guestlist.clubsListFragment;

import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.application.club.guestlist.MainActivity;
import com.application.club.guestlist.R;
import com.application.club.guestlist.booking.BookGuestListActivity;
import com.application.club.guestlist.booking.BookPassActivity;
import com.application.club.guestlist.bookingTable.TableBookingActivity;
import com.application.club.guestlist.bookingTable.TableSelectionActivity;
import com.application.club.guestlist.bookingTable.TableSelectionWebClientActivity;
import com.application.club.guestlist.clubdetails.ClubDetailsListActivity;
import com.application.club.guestlist.clubdetails.ClubEventsDetailsItem;
import com.application.club.guestlist.clubdetails.TicketDetailsItem;
import com.application.club.guestlist.login.LoginActivity;
import com.application.club.guestlist.menu.ChangeLocationActivity;
import com.application.club.guestlist.service.EventListener;
import com.application.club.guestlist.service.SocketOperator;

import org.json.JSONArray;
import org.json.JSONObject;

import com.application.club.guestlist.utils.Constants;
import com.application.club.guestlist.utils.UtillMethods;
import com.application.club.guestlist.videoMode.Feed;
import com.application.club.guestlist.videoMode.Video;
import com.application.club.guestlist.videoUI.CenterLayoutManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
//import com.hoanganhtuan95ptit.autoplayvideorecyclerview.AutoPlayVideoRecyclerView;
import com.application.club.guestlist.autoplayvideorecyclerview.AutoPlayVideoRecyclerView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.application.club.guestlist.utils.Constants.LAT_LONG;

//http://android-er.blogspot.com/2016/04/request-location-updates-with.html

public class ClubsListFragment extends Fragment implements
        EventListener {

    SocketOperator socketOperator  = new SocketOperator(this);
    boolean getData = false;
    static JSONArray clubsEventListJsonArray;
    static JSONArray ticketDetailsListJsonArray;
    static JSONArray tableDetailsListJsonArray;
    String clubId;
    String clubname;
    String eventDate;
    String djname;
    String music;
    String imageURL;
    String isNotification;
    String passdiscount;
    String tablediscount;
    String location;

    private ArrayList<ClubEventsDetailsItem> clubEventDetailsItemList;
    private ArrayList<TicketDetailsItem> ticketDetailsItemList;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        return inflater.inflate(R.layout.offer_display_activity, null, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
        //getActivity().getSupportActionBar().setTitle("City");

        clubId = getArguments().getString(Constants.CLUB_ID);
        clubname = getArguments().getString(Constants.CLUB_NAME);
        djname = getArguments().getString(Constants.DJ_NAME);
        music = getArguments().getString(Constants.MUSIC);


        eventDate  = UtillMethods.getTodayDate();
//        clubId  = "99999";
//        clubname  = "True Tramm Trunk";//intent.getStringExtra(Constants.CLUB_NAME);
//
//        djname = "vicky";//intent.getStringExtra(Constants.DJ_NAME);
//        music = "hiphop";//ntent.getStringExtra(Constants.MUSIC););

        imageURL = "/images/club/truetrammtrunk/truetrammtrunk.png";//intent.getStringExtra(Constants.IMAGE_URL);UNT);

        String day = UtillMethods.getDayFromDate(eventDate);

        TextView daytv = (TextView) getActivity().findViewById(R.id.day);
        daytv.setText(day);

        TextView datetv = (TextView) getActivity().findViewById(R.id.date);
        datetv.setText(eventDate);

        TextView clubNametv = (TextView) getActivity().findViewById(R.id.club);
        clubNametv.setText(clubname);

        populateEventsListForClub();

        TextView djtv = (TextView) getActivity().findViewById(R.id.dj);
        djtv.setText(djname);

        TextView musictv = (TextView) getActivity().findViewById(R.id.musicx);
        musictv.setText(music);

//        if(passdiscount != null && !passdiscount.equalsIgnoreCase("0")){
//            TextView passDiscounttv = (TextView) getActivity().findViewById(R.id.passdiscount);
//            passDiscounttv.setText("PASS: "+passdiscount+"% Off");
//            passDiscounttv.setVisibility(View.VISIBLE);
//        }
//
//        if(tablediscount != null && !tablediscount.equalsIgnoreCase("0")){
//            TextView tableDiscounttv = (TextView) getActivity().findViewById(R.id.tablediscount);
//            tableDiscounttv.setText("TABLE: "+tablediscount+"% Off");
//            tableDiscounttv.setVisibility(View.VISIBLE);
//        }


        ImageView mainImagetv = (ImageView) getActivity().findViewById(R.id.mainImage);

        Picasso.with(getActivity().getApplicationContext()).load(Constants.HTTP_URL+imageURL).into(mainImagetv);

//        TextView timetv = (TextView) findViewById(R.id.time);
//        timetv.setText("TIME    "+startTime);

        TextView guestList = (TextView) getActivity().findViewById(R.id.guestList);
        TextView table = (TextView) getActivity().findViewById(R.id.table);
        TextView pass = (TextView) getActivity().findViewById(R.id.pass);




        final Intent intentG = new Intent(getActivity(), BookGuestListActivity.class);
        final Intent intentP = new Intent(getActivity(), BookPassActivity.class);
        final Intent intentT = new Intent(getActivity(), TableSelectionWebClientActivity.class);


        guestList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Intent intentx = new Intent(this, BookGuestListActivity.class);
                intentG.putExtra(Constants.CLUB_ID, clubId);
                intentG.putExtra(Constants.CLUB_NAME, clubname);
                intentG.putExtra(Constants.EVENTDATE, eventDate);
                intentG.putExtra(Constants.IMAGE_URL, imageURL);
                intentG.putExtra(Constants.TICKET_DETAILS, ticketDetailsListJsonArray.toString());
                startActivity(intentG);

            }
        });

        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intentP.putExtra(Constants.CLUB_ID, clubId);
                intentP.putExtra(Constants.CLUB_NAME, clubname);
                intentP.putExtra(Constants.EVENTDATE, eventDate);
                intentP.putExtra(Constants.IMAGE_URL, imageURL);
                intentP.putExtra(Constants.PASS_DISCOUNT, passdiscount);
                intentP.putExtra(Constants.TICKET_DETAILS, ticketDetailsListJsonArray.toString());
                startActivity(intentP);

            }
        });

        table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(mContext, TableBookingActivity.class);
                intentT.putExtra(Constants.CLUB_ID, clubId);
                intentT.putExtra(Constants.CLUB_NAME, clubname);
                intentT.putExtra(Constants.EVENTDATE, eventDate);
                intentT.putExtra(Constants.IMAGE_URL, imageURL);
                intentT.putExtra(Constants.TABLE_DISCOUNT, tablediscount);
                intentT.putExtra(Constants.TICKET_DETAILS, tableDetailsListJsonArray.toString());
                startActivity(intentT);

            }
        });



    }


    private void populateEventsListForClub() {


        try{

            JSONObject loadClubEventsListFromDatabase = new JSONObject();
            loadClubEventsListFromDatabase.put("action", "getEventDetailsForOfferFromDatabase");
            loadClubEventsListFromDatabase.put(Constants.CLUB_ID, clubId);
            loadClubEventsListFromDatabase.put(Constants.EVENTDATE, eventDate);
            socketOperator.sendMessage(loadClubEventsListFromDatabase);


            while(!getData){
                SystemClock.sleep(1000);
            }

            clubEventDetailsItemList = new ArrayList<ClubEventsDetailsItem>();

            if(clubsEventListJsonArray != null){

                for(int i=0; i < clubsEventListJsonArray.length(); i++){
                    ClubEventsDetailsItem clubEventsDetailsItemObj = new ClubEventsDetailsItem();
                    JSONObject clubEventJObj = clubsEventListJsonArray.getJSONObject(i);




                    String clubid = clubEventJObj.getString(Constants.CLUB_ID);
                    String clubname = clubEventJObj.getString(Constants.CLUB_NAME);
                    djname = clubEventJObj.getString(Constants.DJ_NAME);
                    music = clubEventJObj.getString(Constants.MUSIC_TYPE);
                    String date = clubEventJObj.getString(Constants.EVENTDATE);
                    String imageURL = clubEventJObj.getString(Constants.IMAGE_URL);

                    clubEventsDetailsItemObj.setClubname(clubname);
                    clubEventsDetailsItemObj.setClubid(clubid);
                    clubEventsDetailsItemObj.setDjname(djname);
                    clubEventsDetailsItemObj.setMusic(music);
                    clubEventsDetailsItemObj.setDate(date);
                    clubEventsDetailsItemObj.setImageURL(imageURL);

                    clubEventDetailsItemList.add(clubEventsDetailsItemObj);



                }

            }

            ticketDetailsItemList = new ArrayList<TicketDetailsItem>();

            if(ticketDetailsListJsonArray != null){

                for(int i=0; i < ticketDetailsListJsonArray.length(); i++){
                    TicketDetailsItem ticketDetailsItemObj = new TicketDetailsItem();
                    JSONObject ticketDetailJObj = ticketDetailsListJsonArray.getJSONObject(i);


                    String clubid = ticketDetailJObj.getString(Constants.CLUB_ID);
                    String clubname = ticketDetailJObj.getString(Constants.CLUB_NAME);

                    String type = ticketDetailJObj.getString(Constants.TICKET_TYPE);
                    String cost = ticketDetailJObj.getString(Constants.COST);
                    String details = ticketDetailJObj.getString(Constants.DETAILS);

                    String day = ticketDetailJObj.getString(Constants.DAY);
                    String date = ticketDetailJObj.getString(Constants.EVENTDATE);
                    String totaltickets = ticketDetailJObj.getString(Constants.TOTAL_TICKETS);
                    String availbletickets = ticketDetailJObj.getString(Constants.AVAILBLE_TICKETS);


                    ticketDetailsItemObj.setClubname(clubname);
                    ticketDetailsItemObj.setClubid(clubid);
                    ticketDetailsItemObj.setType(type);
                    ticketDetailsItemObj.setCost(cost);
                    ticketDetailsItemObj.setDetails(details);
                    ticketDetailsItemObj.setDay(day);
                    ticketDetailsItemObj.setDate(date);
                    ticketDetailsItemObj.setTotaltickets(totaltickets);
                    ticketDetailsItemObj.setAvailbletickets(availbletickets);

                    ticketDetailsItemList.add(ticketDetailsItemObj);



                }

            }


        }catch (Exception ex){
            ex.printStackTrace();
        }

        // Create the adapter to convert the array to views
//        ClubsDetailListAdapter adapter = new ClubsDetailListAdapter(this, clubEventDetailsItemList);
//
//        adapter.setTicketDetailsListJsonArray(ticketDetailsListJsonArray);
//
//        Constants.setTicketDetailsItemList(ticketDetailsItemList);
//        // Attach the adapter to a ListView
//        ListView listView = (ListView) this.findViewById(R.id.lvUsers);
//        listView.setAdapter(adapter);
    }

    public void eventReceived(String message){
        // conver message to list
        if(message != null){

            try{
                JSONObject eventJObjX = new JSONObject(message);
                clubsEventListJsonArray = eventJObjX.getJSONArray("eventsDetailList");
                ticketDetailsListJsonArray = eventJObjX.getJSONArray("ticketDetailsList");
                tableDetailsListJsonArray = eventJObjX.getJSONArray("tableDetailsList");


            }catch (Exception ex){
                ex.printStackTrace();

            }

        }


        getData = true;



    }


//    @Override
//    public void onBackPressed() {
//
//        finish();
//        Intent intent = new Intent(OfferDisplayActivity.this, MainActivity.class);
//        startActivity(intent);
//    }


}
