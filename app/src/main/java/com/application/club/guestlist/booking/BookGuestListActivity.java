package com.application.club.guestlist.booking;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.application.club.guestlist.R;
import com.application.club.guestlist.qrcode.QRCodeActivity;
import com.application.club.guestlist.service.EventListener;
import com.application.club.guestlist.service.SocketOperator;
import com.application.club.guestlist.utils.Constants;
import com.application.club.guestlist.utils.UtillMethods;

import org.json.JSONObject;


public class BookGuestListActivity extends AppCompatActivity implements EventListener {

    public static final String PREFS_NAME = "LoginPrefs";

    private int pricePerProduct = 180;

    String selectedRadioButton = "";

    SocketOperator socketOperator  = new SocketOperator(this);

    boolean isTicketBooked = false;
    boolean isrecivedData = false;

    String bookingDetails="";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_guest_list);
        getSupportActionBar().setTitle("Guest List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button button = (Button) findViewById(R.id.done);
        Intent intent = getIntent();
        final String eventDate  = intent.getStringExtra(Constants.EVENTDATE);
        final String clubName = intent.getStringExtra(Constants.CLUB_NAME);
        final String clubId = intent.getStringExtra(Constants.CLUB_ID);
        final String imageURL = intent.getStringExtra(Constants.IMAGE_URL);


        String imgURL = Constants.HTTP_URL+imageURL;


        ImageView imgIcon = (ImageView) findViewById(R.id.mainImage);
        Picasso.with(this.getApplicationContext()).load(imgURL).into(imgIcon);

        TextView datetv = (TextView) findViewById(R.id.date);
        datetv.setText(eventDate);
        String dayx = UtillMethods.getDayFromDate(eventDate);
        TextView daytv = (TextView) findViewById(R.id.day);
        daytv.setText(dayx);

        // Capture button clicks
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                if(!selectedRadioButton.equalsIgnoreCase("")){

                    if(selectedRadioButton.equalsIgnoreCase("couple")){
                        bookingDetails = "One couple is allowed";
                    }else if(selectedRadioButton.equalsIgnoreCase("girls")){
                        bookingDetails="Max Three girls allowed";
                    }
                    // Start NewActivity.class
                    Intent intent = new Intent(BookGuestListActivity.this,
                            QRCodeActivity.class);
                    intent.putExtra(Constants.BOOKING_TYPE, "guest list");
                    intent.putExtra(Constants.SELECTED_GUEST_TYPE, selectedRadioButton);
                    intent.putExtra(Constants.EVENTDATE, eventDate);
                    intent.putExtra(Constants.CLUB_ID, clubId);
                    intent.putExtra(Constants.CLUB_NAME, clubName);

                    long time= System.currentTimeMillis();
                    final String qrNumber = Long.toString(time);

                    intent.putExtra(Constants.QRNUMBER, qrNumber);

                    try{

                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                        String custmerName = null;
                        String custmerMobile = null;
                        String customerId = null;
                        if (settings.getString("logged", "").toString().equals("logged")) {

                            custmerName = settings.getString("name","");
                            custmerMobile = settings.getString("mobile","");
                            customerId = settings.getString(Constants.CUSTOMERID,"");

                        }


                        JSONObject ticketBookingDetails = new JSONObject();
                        ticketBookingDetails.put("action", "inserOrderDetails");
                        ticketBookingDetails.put(Constants.TICKETTYPE, "guest list");
                        ticketBookingDetails.put(Constants.TICKET_DETAILS, bookingDetails);
                        ticketBookingDetails.put(Constants.EVENTDATE, eventDate);
                        ticketBookingDetails.put(Constants.CLUB_ID, clubId);
                        ticketBookingDetails.put(Constants.CLUB_NAME, clubName);
                        ticketBookingDetails.put(Constants.QRNUMBER, qrNumber);
                        ticketBookingDetails.put(Constants.CUSTOMERNAME, clubName);
                        ticketBookingDetails.put(Constants.MOBILE, custmerMobile);
                        ticketBookingDetails.put(Constants.CUSTOMERID, clubId);
                        ticketBookingDetails.put(Constants.COST, "0");
                        ticketBookingDetails.put(Constants.COSTAFTERDISCOUNT, "0");
                        ticketBookingDetails.put(Constants.PAID_AMOUNT, "0");
                        ticketBookingDetails.put(Constants.REMAINING_AMOUNT, "0");
                        ticketBookingDetails.put(Constants.DISCOUNT, "0");
                        //String todayDate = UtillMethods.getTodayDate();
                        //ticketBookingDetails.put(Constants.BOOKINGDATE, todayDate);



                    socketOperator.sendMessage(ticketBookingDetails);

                    }catch (Exception ex){
                        ex.printStackTrace();
                    }


                    while(!isrecivedData){
                        SystemClock.sleep(1000);
                    }
                    if(isTicketBooked){
                        startActivity(intent);
                    }else{
                        Toast.makeText(BookGuestListActivity.this, "You have already booked one Guest List for "+eventDate, Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(BookGuestListActivity.this, "Please select one option", Toast.LENGTH_LONG).show();
                }

            }
        });

        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.couple:
                        selectedRadioButton = "couple";
                        break;
                    case R.id.girls:
                        selectedRadioButton = "girls";
                        break;

                }
            }
        });



    }

    @Override
    public boolean onSupportNavigateUp(){

        finish();
        return true;
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
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



    public void eventReceived(String message){
        // conver message to list
        if(message != null){

            try{
                if(message.equalsIgnoreCase("success")){
                    isTicketBooked = true;
                }else{
                    isTicketBooked = false;
                }
                isrecivedData = true;


            }catch (Exception ex){
                ex.printStackTrace();

            }

        }






    }
}
