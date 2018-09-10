package com.application.club.guestlist.bookingTable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.application.club.guestlist.MainActivity;
import com.application.club.guestlist.R;
import com.application.club.guestlist.clubdetails.TableDetailsItem;
import com.application.club.guestlist.service.EventListener;
import com.application.club.guestlist.utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.xm.weidongjian.popuphelper.PopupWindowHelper;

import static com.application.club.guestlist.utils.Constants.CLUB_NAME;
import static com.application.club.guestlist.utils.Constants.LAYOUT_URL;

public class TableSelectionWebClientActivity extends AppCompatActivity implements View.OnClickListener{
    /** Called when the activity is first created. */

    WebView web;
    private PopupWindowHelper popupWindowHelper;
    //final private View popView;
    private Context context;
    String tableID;
    String layoutURL;
    String evntDate;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_selection_webclient_activity);

        getSupportActionBar().setTitle("Select Table");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Map<String, TableDetailsItem> tableMap = new HashMap<>();


        Intent intent = getIntent();
        final String clubName  = intent.getStringExtra(CLUB_NAME);
        final String clubidx  = intent.getStringExtra(Constants.CLUB_ID);
        final String date = intent.getStringExtra(Constants.EVENTDATE);
        final String imageURL = intent.getStringExtra(Constants.IMAGE_URL);
        final String tableDiscount = intent.getStringExtra(Constants.TABLE_DISCOUNT);
        final String ticketDetailsJsonArryStr = intent.getStringExtra(Constants.TICKET_DETAILS);
        evntDate = intent.getStringExtra(Constants.EVENTDATE);
        JSONArray ticketDetailsListJsonArray = null;

        final TextView tv = findViewById(R.id.popup);

        final View popView = LayoutInflater.from(this).inflate(R.layout.popupview, null);
        popupWindowHelper = new PopupWindowHelper(popView);
        popView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                popupWindowHelper.dismiss();
                //Toast.makeText(TableSelectionWebClientActivity.this, "Button Clicked",	Toast.LENGTH_SHORT).show();
            }

        });
        popView.findViewById(R.id.booK).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Toast.makeText(TableSelectionWebClientActivity.this, "Button Clicked",	Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TableSelectionWebClientActivity.this, TableConfirmationActivity.class);
                intent.putExtra(Constants.CLUB_ID, clubidx);
                intent.putExtra(Constants.CLUB_NAME, clubName);
                intent.putExtra(Constants.EVENTDATE, date);
                intent.putExtra(Constants.TABLE_ID, tableMap.get(tableID).getTableid());
                intent.putExtra(Constants.COST, tableMap.get(tableID).getCost());
                intent.putExtra(Constants.TABLE_NUMBER, tableMap.get(tableID).getTableNumber());
                intent.putExtra(Constants.TABLE_TYPE, tableMap.get(tableID).getTabletype());
                intent.putExtra(Constants.DETAILS, tableMap.get(tableID).getDetails());
                intent.putExtra(Constants.TABLE_SIZE, tableMap.get(tableID).getSize());
                intent.putExtra(Constants.TABLE_DISCOUNT, tableDiscount);
                intent.putExtra(Constants.IMAGE_URL, imageURL);
                startActivity(intent);
            }

        });



        try {

            ticketDetailsListJsonArray = new JSONArray(ticketDetailsJsonArryStr);

            if (ticketDetailsListJsonArray != null) {

                for(int i=0; i < ticketDetailsListJsonArray.length(); i++){

                    //TableDetailsItem ticketDetailsItemObj = new TableDetailsItem();
                    JSONObject tableDetailJObj = ticketDetailsListJsonArray.getJSONObject(i);
                    String clubid = tableDetailJObj.getString(Constants.CLUB_ID);
                    String clubNamex = tableDetailJObj.getString(Constants.CLUB_NAME);
                    String tableID = tableDetailJObj.getString(Constants.TABLE_ID);
                    String tableNumber = tableDetailJObj.getString(Constants.TABLE_NUMBER);
                    String details = tableDetailJObj.getString(Constants.DETAILS);
                    String tabletype = tableDetailJObj.getString(Constants.TABLE_TYPE);
                    String size = tableDetailJObj.getString(Constants.SIZE);
                    String cost = tableDetailJObj.getString(Constants.COST);
                    String eventDate = tableDetailJObj.getString(Constants.EVENT_DATE);
                    String booked = tableDetailJObj.getString(Constants.ISBOOKED);

                    layoutURL = tableDetailJObj.getString(Constants.LAYOUT_URL);

                    TableDetailsItem tr = new TableDetailsItem();
                    tr.setClubid(clubidx);
                    tr.setClubname(clubNamex);
                    tr.setTableid(tableID);
                    tr.setTableNumber(tableNumber);
                    tr.setDetails(details);
                    tr.setTabletype(tabletype);
                    tr.setSize(size);
                    tr.setCost(cost);
                    tr.setEventdate(eventDate);
                    tr.setBooked(booked);

                    tableMap.put(tableID, tr);


                }

            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        web = (WebView) findViewById(R.id.webview01);
        web.setWebViewClient(new myWebClient());
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setAppCacheEnabled(false);
        web.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        String eventdate = date.replaceAll("/", "");
        String destFileName = clubidx+"-"+eventdate+".html";
        //web.loadUrl(Constants.HTTP_LAYOUT_HOST+Constants.TABLE_LAYOUT_URL+destFileName);
        //web.loadUrl("http://172.20.10.10/imagemap/t3.html");
        web.loadUrl(layoutURL);
        web.setWebViewClient(new WebViewClient()
             {
                 public void onPageFinished(WebView view, String url)
                 {
                     Log.e("String HrefValue :-",url);
                     if(url.contains("#")){
                         Log.e("String HrefValue :-",url);
                         String[] tocken = url.split("#");
                         tableID = tocken[1];
                         TableDetailsItem tdi = tableMap.get(tableID);
                         if(!tdi.getBooked().equalsIgnoreCase("booked")){
                             popupWindowHelper.showFromBottom(tv);
                         }else if(tdi.getBooked().equalsIgnoreCase("booked")){
                             Toast toast =  Toast.makeText(TableSelectionWebClientActivity.this, "Not Available !",	Toast.LENGTH_SHORT);
                             toast.setGravity(Gravity.CENTER, 0, 0);
                             toast.show();
                         }

                         TextView tableNumber = (TextView) popView.findViewById(R.id.tableNumberValue);
                         tableNumber.setText(tdi.getTableNumber());
                         TextView detailsValue = (TextView) popView.findViewById(R.id.detailsValue);
                         detailsValue.setText(tdi.getDetails());
                         TextView guestCountValue = (TextView) popView.findViewById(R.id.guestCountValue);
                         guestCountValue.setText(tdi.getSize());
                         TextView costValue = (TextView) popView.findViewById(R.id.costValue);
                         costValue.setText(tdi.getCost());
                     }


                     //TextView some = (TextView) popView.findViewById(R.id.tableNumber);


                 }
             }

        );
    }

    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;

        }


    }

    @Override
    public void onClick(View v) {

        popupWindowHelper.dismiss();
    }

    // To handle "Back" key press event for WebView to go back to previous screen.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && web.canGoBack()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(Constants.EVENT_DATE, evntDate);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.EVENT_DATE, evntDate);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        finish();
        return true;
    }




}
