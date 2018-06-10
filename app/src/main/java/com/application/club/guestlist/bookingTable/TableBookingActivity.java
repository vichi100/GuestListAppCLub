package com.application.club.guestlist.bookingTable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.application.club.guestlist.R;
import com.application.club.guestlist.clubdetails.ClubDetailsListActivity;
import com.application.club.guestlist.clubdetails.TicketDetailsItem;
import com.application.club.guestlist.utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.application.club.guestlist.utils.Constants.CLUB_NAME;

/**
 * Created by Oclemmy on 5/10/2016 for ProgrammingWizards Channel and http://www.Camposha.com.
 */
public class TableBookingActivity extends AppCompatActivity  implements AdapterView.OnItemClickListener {



    TableBookingListAdapter adapter;
    private ArrayList<TicketDetailsItem> ticketDetailsrowItems;
    ListView list;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_booking_activity);
        getSupportActionBar().setTitle("Table Booking");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        final String clubName  = intent.getStringExtra(CLUB_NAME);
        final String clubidx  = intent.getStringExtra(Constants.CLUB_ID);
        final String date = intent.getStringExtra(Constants.EVENTDATE);
        final String imageURL = intent.getStringExtra(Constants.IMAGE_URL);
        final String tableDiscount = intent.getStringExtra(Constants.TABLE_DISCOUNT);
        final String ticketDetailsJsonArryStr = intent.getStringExtra(Constants.TICKET_DETAILS);
        JSONArray ticketDetailsListJsonArray = null;
        ticketDetailsrowItems = new ArrayList<TicketDetailsItem>();
        try {

            ticketDetailsListJsonArray = new JSONArray(ticketDetailsJsonArryStr);

            if(ticketDetailsListJsonArray != null){

                for(int i=0; i < ticketDetailsListJsonArray.length(); i++){
                    TicketDetailsItem ticketDetailsItemObj = new TicketDetailsItem();
                    JSONObject ticketDetailJObj = ticketDetailsListJsonArray.getJSONObject(i);
                    String type = ticketDetailJObj.getString(Constants.TICKET_TYPE);
                    String category = ticketDetailJObj.getString(Constants.CATEGORY);
                    String eventdate = ticketDetailJObj.getString(Constants.EVENTDATE);
//                    String clubid = ticketDetailJObj.getString(Constants.CLUB_ID);
//                    String clubname = ticketDetailJObj.getString(Constants.CLUB_NAME);
                    if(type.equalsIgnoreCase("table") && date.equalsIgnoreCase(eventdate)){

                        String size = ticketDetailJObj.getString(Constants.TABLE_SIZE);
                        String cost = ticketDetailJObj.getString(Constants.COST);
                        String details = ticketDetailJObj.getString(Constants.DETAILS);
                        String datex = ticketDetailJObj.getString(Constants.EVENTDATE);
                        //String imgURL = ticketDetailJObj.getString(Constants.IMAGE_URL);

                        ticketDetailsItemObj.setSize(size);
                        ticketDetailsItemObj.setDetails(details);
                        ticketDetailsItemObj.setCost(cost);
                        ticketDetailsItemObj.setDate(datex);
                        ticketDetailsItemObj.setClubid(clubidx);
                        ticketDetailsItemObj.setClubname(clubName);
                        ticketDetailsItemObj.setImageUrl(imageURL);
                        ticketDetailsrowItems.add(ticketDetailsItemObj);

                    }

                }

            }

        }catch (Exception ex){
            ex.printStackTrace();
        }



        adapter = new TableBookingListAdapter(this, ticketDetailsrowItems);

        list=(ListView)findViewById(R.id.listView);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(TableBookingActivity.this, "vichi", Toast.LENGTH_SHORT).show();
                TicketDetailsItem ticketDetailItemsObj = ticketDetailsrowItems.get(position);
                String clubId = ticketDetailItemsObj.getClubid();
                String clubName =  ticketDetailItemsObj.getClubname();
                String date = ticketDetailItemsObj.getDate();
                String details = ticketDetailItemsObj.getDetails();
                String cost = ticketDetailItemsObj.getCost();
                String size = ticketDetailItemsObj.getSize();
                String imageURL = ticketDetailItemsObj.getImageUrl();

                Intent intent = new Intent(TableBookingActivity.this, TableConfirmationActivity.class);
                intent.putExtra(Constants.CLUB_ID, clubId);
                intent.putExtra(Constants.CLUB_NAME, clubName);
                intent.putExtra(Constants.EVENTDATE, date);
                intent.putExtra(Constants.COST, cost);
                intent.putExtra(Constants.DETAILS, details);
                intent.putExtra(Constants.TABLE_SIZE, size);
                intent.putExtra(Constants.TABLE_DISCOUNT, tableDiscount);
                intent.putExtra(Constants.IMAGE_URL, imageURL);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){

        finish();
        return true;
    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        } else if (id == android.R.id.home) {
//            finish();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

//        Toast.makeText(getActivity(), menutitles[position], Toast.LENGTH_SHORT)
//                .show();
        // we will get jasson array list here

        TicketDetailsItem ticketDetailItemsObj = ticketDetailsrowItems.get(position);
        String clubId = ticketDetailItemsObj.getClubid();
        Intent intent = new Intent(this, ClubDetailsListActivity.class);
        intent.putExtra(Constants.CLUB_ID, clubId);
        startActivity(intent);

    }

}
