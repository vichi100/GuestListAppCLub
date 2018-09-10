package com.application.club.guestlist.clubdetails;
import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.application.club.guestlist.MainActivity;
import com.github.florent37.tutoshowcase.TutoShowcase;
import com.squareup.picasso.Picasso;
import com.application.club.guestlist.R;
import com.application.club.guestlist.service.EventListener;
import com.application.club.guestlist.service.SocketOperator;
import com.application.club.guestlist.utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

import static com.application.club.guestlist.utils.Constants.CLUB_ID;
import static com.application.club.guestlist.utils.Constants.HTTP_URL;

public class ClubDetailsListActivity extends AppCompatActivity implements EventListener {

	SocketOperator socketOperator  = new SocketOperator(this);
	boolean getData = false;
	static JSONArray clubsEventListJsonArray;
	static JSONArray ticketDetailsListJsonArray;

	private ArrayList<ClubEventsDetailsItem> clubEventDetailsItemList;
    private ArrayList<TicketDetailsItem> ticketDetailsItemList;

    String clubId;
    String clubName;

	private static final String SHOWCASE_ID = "simple example";

	ListView clubListv;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.club_details_activity_custom_list);
		//getSupportActionBar().setTitle("Events");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		clubListv = (ListView)findViewById(R.id.lvUsers);
		Intent intent = getIntent();
        clubName = intent.getStringExtra(Constants.CLUB_NAME);
        getSupportActionBar().setTitle(clubName);
        clubId = intent.getStringExtra(CLUB_ID);
//		String imageURL = intent.getStringExtra(Constants.IMAGE_URL);
//		try{
//
//			Picasso.with(this)
//					.load(HTTP_URL+imageURL).into(clubimagetv);
//		}catch (Exception ex){
//			Picasso.with(this)
//					.load(imageURL).into(clubimagetv);
//		}


		populateEventsListForClub();
       // displayTuto(); // one second delay
	}

	private void populateEventsListForClub() {


		try{

			JSONObject loadClubEventsListFromDatabase = new JSONObject();
			loadClubEventsListFromDatabase.put("action", "getEventDetailsFromDatabase");
            loadClubEventsListFromDatabase.put(Constants.CLUB_ID, clubId);
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
					String djname = clubEventJObj.getString(Constants.DJ_NAME);
					String music = clubEventJObj.getString(Constants.MUSIC_TYPE);
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
		ClubsDetailListAdapter adapter = new ClubsDetailListAdapter(this, clubEventDetailsItemList);

        adapter.setTicketDetailsListJsonArray(ticketDetailsListJsonArray);

        Constants.setTicketDetailsItemList(ticketDetailsItemList);
		// Attach the adapter to a ListView
		ListView listView = (ListView) this.findViewById(R.id.lvUsers);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {

				Toast.makeText(ClubDetailsListActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(ClubDetailsListActivity.this, MainActivity.class);
				String eventDate = clubEventDetailsItemList.get(position).getDate();
				intent.putExtra(Constants.EVENT_DATE, eventDate);
				startActivity(intent);

			}

		});
	}

	public void eventReceived(String message){
		// conver message to list
		if(message != null){

			try{
				JSONObject eventJObjX = new JSONObject(message);
				clubsEventListJsonArray = eventJObjX.getJSONArray("eventsDetailList");
				ticketDetailsListJsonArray = eventJObjX.getJSONArray("ticketDetailsList");


			}catch (Exception ex){
				ex.printStackTrace();

			}

		}


		getData = true;



	}

	@Override
    public boolean onSupportNavigateUp(){

        finish();
        return true;
    }

    protected void displayTuto() {
        TutoShowcase.from(this)
                .setListener(new TutoShowcase.Listener() {
                    @Override
                    public void onDismissed() {
                        Toast.makeText(ClubDetailsListActivity.this, "Tutorial dismissed", Toast.LENGTH_SHORT).show();
                    }
                })
                .setContentView(R.layout.club_details_activity_custom_list)
                .setFitsSystemWindows(true)
                //.on(R.id.about)
                //.addCircle()
                //.withBorder()
//                .onClick(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                })

                //.on(R.id.swipable)
                //.displaySwipableLeft()
                //.delayed(399)
                //.animated(true)
                .show();
    }
	
}
