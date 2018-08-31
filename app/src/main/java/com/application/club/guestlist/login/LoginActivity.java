package com.application.club.guestlist.login;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;


import com.application.club.guestlist.MainActivity;
import com.application.club.guestlist.R;
import com.application.club.guestlist.offer.OfferDisplayActivity;
import com.application.club.guestlist.service.EventListener;
import com.application.club.guestlist.service.SocketOperator;
import com.application.club.guestlist.utils.Constants;
import com.application.club.guestlist.utils.UtillMethods;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DateFormat;

/**
 * Created by vichi on 11/01/18.
 */

public class LoginActivity extends  AppCompatActivity
        implements EventListener {

    private static final String TAG = LoginActivity.class.getSimpleName();

    Button button;

    SocketOperator socketOperator  = new SocketOperator(this);

    boolean isCustomerCreated = false;

    boolean startMainActivityFlag = false;
    boolean isGetResponseFromDB = false;

    String customerId = null;

    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;

    String latlong;

    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    JSONObject eventDetailsObj;

    String eventDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        eventDate = UtillMethods.getTodayDate();

        /*
         * Check if we successfully logged in before.
         * If we did, redirect to home page
         */
        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);

        if (settings.getString("logged", "").toString().equals("logged")) {
            String clubname = settings.getString("clubname","");
            String clubid = settings.getString("clubid","");
            String password = settings.getString("password","");
            try{
                JSONObject customerDetails = new JSONObject();
                customerDetails.put("action", "clubLogindDataAndValidation");
                customerDetails.put(Constants.CLUB_NAME, clubname);
                customerDetails.put(Constants.CLUB_ID, clubid);
                customerDetails.put(Constants.PASSWORD, password);
                customerDetails.put(Constants.EVENT_DATE, eventDate);

                socketOperator.sendMessage(customerDetails);

                while(!isGetResponseFromDB){
                    SystemClock.sleep(1000);
                }

            }catch (Exception ex){
                ex.printStackTrace();
            }

            if(startMainActivityFlag){

                try{

                    Intent intent = new Intent(LoginActivity.this,
                            MainActivity.class);
                    //SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(Constants.DJ_NAME, eventDetailsObj.getString(Constants.DJ_NAME));
                    editor.putString(Constants.MUSIC, eventDetailsObj.getString(Constants.MUSIC));
                    editor.commit();

//                    intent.putExtra(Constants.CLUB_NAME, eventDetailsObj.getString(Constants.CLUB_NAME));
//                    intent.putExtra(Constants.CLUB_ID, clubid);
//                    intent.putExtra(Constants.DJ_NAME, eventDetailsObj.getString(Constants.DJ_NAME));
//                    intent.putExtra(Constants.MUSIC, eventDetailsObj.getString(Constants.MUSIC));
//                    intent.putExtra(Constants.EVENT_DATE, eventDate);
                    startActivity(intent);

                }catch (Exception ex){
                    ex.printStackTrace();
                }



            }else{
                Toast.makeText(LoginActivity.this, "Password is expired, please contact support!", Toast.LENGTH_LONG).show();

            }

        }
        // Locate the button in activity_main.xml
        button = (Button) findViewById(R.id.next);

        // Capture button clicks
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {

                EditText clubnametv = (EditText) findViewById(R.id.cname);
                final String clubname = clubnametv.getText().toString();
                EditText clubidtv = (EditText) findViewById(R.id.clubid);
                final String clubid = clubidtv.getText().toString();
                final EditText passwordtv = (EditText) findViewById(R.id.cmobile);//password
                final String password = passwordtv.getText().toString();
                EditText confirmPasswordtv = (EditText) findViewById(R.id.ccmobile);//confirm password
                final String confirmPassword = confirmPasswordtv.getText().toString();

                if((null != clubname && !clubname.trim().equalsIgnoreCase("")
                        && null != clubid && !clubid.trim().equalsIgnoreCase(""))) {

                    if (null != password && password.trim().length() >= 1) {

                        if (null != confirmPassword && confirmPassword.trim().length() >= 1 && password.equalsIgnoreCase(confirmPassword)) {


                            /*
                             * So login information is correct,
                             * we will save the Preference data
                             * and redirect to next class / home
                             */
                            try{


                                JSONObject customerDetails = new JSONObject();
                                customerDetails.put("action", "clubLogindDataAndValidation");
                                customerDetails.put(Constants.CLUB_NAME, clubname);
                                customerDetails.put(Constants.CLUB_ID, clubid);
                                customerDetails.put(Constants.PASSWORD, confirmPassword);
                                customerDetails.put(Constants.EVENT_DATE, eventDate);

                                socketOperator.sendMessage(customerDetails);

                                while(!isGetResponseFromDB){
                                    SystemClock.sleep(1000);
                                }



                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                            if(startMainActivityFlag){
                                SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("logged", "logged");
                                editor.putString("clubname", clubname);
                                editor.putString("clubid", clubid);
                                editor.putString("password", confirmPassword);
                                editor.commit();

                                Intent intent = new Intent(LoginActivity.this,
                                        MainActivity.class);
                                startActivity(intent);


                            }else{
                                Toast.makeText(LoginActivity.this, "Network is not working or very slow !", Toast.LENGTH_LONG).show();

                            }


                        }else{
                            Toast.makeText(LoginActivity.this, "Password are not matching !", Toast.LENGTH_LONG).show();
                        }


                    }else{
                        Toast.makeText(LoginActivity.this, "Pleae Enter Password Number !", Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(LoginActivity.this, "Pleae Enter Club Name and Club Id !", Toast.LENGTH_LONG).show();
                }


            }
        });
    }


    public void eventReceived(String message){
        // conver message to list
        if(message != null){



            try{
                JSONObject eventJObjX = new JSONObject(message);
                eventDetailsObj = eventJObjX.getJSONObject("eventDetailsObj");
                String isSuccess = eventJObjX.getString("passwordCheckingDone");
                if(!isSuccess.equalsIgnoreCase("fail")){
                    startMainActivityFlag = true;
                }else{
                    startMainActivityFlag = false;
                }
                isGetResponseFromDB = true;

            }catch (Exception ex){
                ex.printStackTrace();

            }

//            try{
//                if(message != null){
//                    isGetResponseFromDB = true;
//                    if(message.equalsIgnoreCase("success")){
//                        startMainActivityFlag = true;
//                    }else{
//                        startMainActivityFlag = false;
//                    }
//
//                }
//
//
//
//            }catch (Exception ex){
//                ex.printStackTrace();
//
//            }

        }
    }


}
