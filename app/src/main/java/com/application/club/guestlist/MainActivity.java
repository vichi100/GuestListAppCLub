package com.application.club.guestlist;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.application.club.guestlist.reports.DailySummaryReport;
import com.application.club.guestlist.upload.UploadFragment;
import com.application.club.guestlist.utils.UtillMethods;
import com.github.florent37.tutoshowcase.TutoShowcase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.application.club.guestlist.db.DBHelper;
import com.application.club.guestlist.firebasenotifications.Config;
import com.application.club.guestlist.firebasenotifications.NotificationUtils;
import com.application.club.guestlist.bookedPasses.BookingFragment;
import com.application.club.guestlist.clubsListFragment.ClubsListFragment;
import com.application.club.guestlist.menu.LocationChangeActivity;
import com.application.club.guestlist.offer.OffersFragment;
import com.application.club.guestlist.profile.ProfileScreenFragment;
import com.application.club.guestlist.utils.Constants;


public class MainActivity extends AppCompatActivity implements AHBottomNavigation.OnTabSelectedListener{

    AHBottomNavigation bottomNavigation;
    DBHelper mydb;
    String clubid  ;
    String clubName;
    String djName;
    String music;
    String eventDate;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        Intent intent = getIntent();
//        clubid  = intent.getStringExtra(Constants.CLUB_ID);
//        clubName  = intent.getStringExtra(Constants.CLUB_NAME);
//        djName = intent.getStringExtra(Constants.DJ_NAME);
//        music = intent.getStringExtra(Constants.MUSIC);
//        String eventDate  = UtillMethods.getTodayDateAsDate().toString();

        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
        clubName = settings.getString("clubname","");
        clubid = settings.getString("clubid","");
        djName = settings.getString(Constants.DJ_NAME,"");
        music = settings.getString(Constants.MUSIC,"");
        eventDate = intent.getStringExtra(Constants.EVENT_DATE);


        bottomNavigation= (AHBottomNavigation) findViewById(R.id.myBottomNavigation_ID);

        bottomNavigation.setOnTabSelectedListener(this);
        this.createNavItems();



//        Bundle bundle = getIntent().getExtras();
//
//        if (bundle != null) {
//            for (String key : bundle.keySet()) {
//                Object value = bundle.get(key);
//                Log.d(TAG, "Key: " + key + " Value: " + value.toString());
//            }
//        }
        //displayTuto(); // one second delay

    }

    @Override
    protected void onNewIntent(Intent intent) {

    }

    @Override
    public void onBackPressed(){

    }

    private void createNavItems()
    {
        //CREATE ITEMS
        AHBottomNavigationItem club =new AHBottomNavigationItem("Club",R.drawable.pub);
        AHBottomNavigationItem charts =new AHBottomNavigationItem("Charts",R.drawable.charts);
        AHBottomNavigationItem qrcode=new AHBottomNavigationItem("QRCode",R.drawable.qrcode);
        AHBottomNavigationItem upload =new AHBottomNavigationItem("Upload",R.drawable.upload);


        //AHBottomNavigationItem passItem=new AHBottomNavigationItem("Bookings",R.drawable.ticket3);


        //AHBottomNavigationItem trending=new AHBottomNavigationItem("Trending",R.drawable.trending);





        //ADD ITEMS TO BAR
        bottomNavigation.addItem(club);
        bottomNavigation.addItem(charts);
        bottomNavigation.addItem(qrcode);
        bottomNavigation.addItem(upload);
        //bottomNavigation.addItem(offer);
        //bottomNavigation.addItem(trending);
        //bottomNavigation.addItem(passItem);


        //PROPERTIES
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#1c1d1d"));

        bottomNavigation.setCurrentItem(0);

    }

    @Override
    public boolean onTabSelected(int position, boolean wasSelected) {
        String title="";
        if(position==0)
        {
            SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
            ClubsListFragment clubsListFragment =new ClubsListFragment();
            //Put the value

            Bundle args = new Bundle();
            //args.putString("YourKey", "YourValue");
            args.putString(Constants.CLUB_ID, clubid);
            args.putString(Constants.CLUB_NAME, clubName);
            args.putString(Constants.DJ_NAME, djName);
            args.putString(Constants.MUSIC, music);
            args.putString(Constants.EVENT_DATE, eventDate);
            clubsListFragment.setArguments(args);


            getSupportFragmentManager().beginTransaction().replace(R.id.content_id, clubsListFragment).commit();
            title = clubName;

        }

        else if(position==1)
        {

            DailySummaryReport dailySummaryReport =new DailySummaryReport();

            getSupportFragmentManager().beginTransaction().replace(R.id.content_id, dailySummaryReport).commit();
            title = "Chart";

        }


        else if(position==2)
        {
            ProfileScreenFragment profileScreenFragment =new ProfileScreenFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_id, profileScreenFragment).commit();

            title = "QRCode";
        }

        else if(position==3)
        {

            UploadFragment uploadFragment =new UploadFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_id, uploadFragment).commit();

            title = "Upload";


        }
//        else if(position==4)
//        {
//
////            DocumentaryFragment documentaryFragment=new DocumentaryFragment();
////            getSupportFragmentManager().beginTransaction().replace(R.id.content_id,documentaryFragment).commit();
////            CrimeFragment crimeFragment=new CrimeFragment();
////            getSupportFragmentManager().beginTransaction().replace(R.id.content_id,crimeFragment).commit();
//
//            //Intent intent = new Intent(this, ProfileScreen.class);
////            this.startActivity(intent);
//
//
//
//
//
////            BookingFragment bookingFragment =new BookingFragment();
////
////
////            getSupportFragmentManager().beginTransaction().replace(R.id.content_id, bookingFragment).commit();
////            title = "Bookings";
//
//            DailySummaryReport dailySummaryReport =new DailySummaryReport();
//
//
//            getSupportFragmentManager().beginTransaction().replace(R.id.content_id, dailySummaryReport).commit();
//            title = "Upload";
//
//
//
//
//
//        }
        //        else if(position==1)
//        {
//
//            OffersFragment offersScreenFragment =new OffersFragment();
//            getSupportFragmentManager().beginTransaction().replace(R.id.content_id, offersScreenFragment).commit();
//
//            title = "Offers";
//
//
//        }

//        else if(position==1)
//        {
//
//            OffersFragment offersScreenFragment =new OffersFragment();
//            getSupportFragmentManager().beginTransaction().replace(R.id.content_id, offersScreenFragment).commit();
//
//            title = "Trending";
//
//
//        }

        updateToolbarText(title);
        return true;
    }

    private void updateToolbarText(CharSequence text) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(text);
        }
    }







    protected void displayTuto() {
        TutoShowcase.from(this)
                .setListener(new TutoShowcase.Listener() {
                    @Override
                    public void onDismissed() {
                        Toast.makeText(MainActivity.this, "Tutorial dismissed", Toast.LENGTH_SHORT).show();
                    }
                })
                .setContentView(R.layout.activity_main)
                .setFitsSystemWindows(true)
                //.on(R.id.myBottomNavigation_ID)
                //.addCircle()
                //.withBorder()
//                .onClick(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                })

//                .on(R.id.swipable)
//                .displaySwipableLeft()
               // .delayed(399)
               // .animated(true)
                .show();
    }
}
