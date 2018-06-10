package com.application.club.guestlist.clubsListFragment;

import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ListFragment;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.content.Context;
import android.widget.Toast;

import com.application.club.guestlist.R;
import com.application.club.guestlist.clubdetails.ClubDetailsListActivity;
import com.application.club.guestlist.login.LoginActivity;
import com.application.club.guestlist.menu.ChangeLocationActivity;
import com.application.club.guestlist.service.EventListener;
import com.application.club.guestlist.service.SocketOperator;

import org.json.JSONArray;
import org.json.JSONObject;

import com.application.club.guestlist.utils.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import static com.application.club.guestlist.utils.Constants.LAT_LONG;

//http://android-er.blogspot.com/2016/04/request-location-updates-with.html

public class ClubsListFragment extends ListFragment implements OnItemClickListener,  SearchView.OnQueryTextListener,
        MenuItem.OnActionExpandListener, EventListener , LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

	String[] menutitles;
	TypedArray menuIcons;

    List<String> mAllValues;
    private Context mContext;
    ClubsListAdapter adapter;

    ClubsListAdapter mAdapter;
    private List<ClubRowItem> clubRowItemList;

    SocketOperator socketOperator  = new SocketOperator(this);
    boolean getClubList = false;

    static JSONArray clubsListJsonArray;

    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;

    String latlong = "";

    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.list_fragment, null, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
        //getActivity().getActionBar().setTitle("City");

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2*60*1000);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setSmallestDisplacement(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();




		menutitles = getResources().getStringArray(R.array.titles);
		menuIcons = getResources().obtainTypedArray(R.array.icons);


	}


	private void getclubListFromDatabase(){
        try{
            SharedPreferences settings = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
            String city = settings.getString(Constants.CITY,"");
            JSONObject loadClubListFromDatabase = new JSONObject();
            loadClubListFromDatabase.put("action", "loadClubListFromDatabase");
            loadClubListFromDatabase.put(Constants.CITY, city);
            loadClubListFromDatabase.put(Constants.LAT_LONG, latlong);


            socketOperator.sendMessage(loadClubListFromDatabase);

        }catch (Exception ex){
            ex.printStackTrace();
        }

        try{


            while(!getClubList){
                SystemClock.sleep(1000);
            }

            clubRowItemList = new ArrayList<ClubRowItem>();

            if(clubsListJsonArray != null){

                for(int i=0; i < clubsListJsonArray.length(); i++){
                    ClubRowItem clubRowItem = new ClubRowItem();
                    JSONObject clubjObj = clubsListJsonArray.getJSONObject(i);


                    String clubid = clubjObj.getString(Constants.CLUB_ID);
                    String clubname = clubjObj.getString(Constants.CLUB_NAME);
                    String city = clubjObj.getString(Constants.CITY);
                    String location = clubjObj.getString(Constants.LOACTION);
                    String address = clubjObj.getString(Constants.ADDRESS);
                    String imageURL = clubjObj.getString(Constants.IMAGE_URL);
                    String latlong = clubjObj.getString(LAT_LONG);
                    String rating = clubjObj.getString(Constants.RATING);

                    clubRowItem.setClubname(clubname);
                    clubRowItem.setClubid(clubid);
                    clubRowItem.setCity(city);
                    clubRowItem.setLocation(location);
                    clubRowItem.setAddress(address);
                    clubRowItem.setImageURL(imageURL);
                    clubRowItem.setLatlong(latlong);
                    clubRowItem.setRating(rating);

                    clubRowItemList.add(clubRowItem);

                    ArrayList<ClubRowItem> x = new ArrayList<>(clubRowItemList);
                    ArrayList<ClubRowItem> y = new ArrayList<>(clubRowItemList);



                }

            }
        }catch (Exception ex){
            ex.printStackTrace();
        }



        adapter = new ClubsListAdapter(getActivity(), clubRowItemList);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search");

//        super.onCreateOptionsMenu(menu, inflater);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
//            case R.id.action_search:
//                newGame();
//                return true;
            case R.id.action_location:
                Intent intent = new Intent(getActivity(), ChangeLocationActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

//        Toast.makeText(getActivity(), menutitles[position], Toast.LENGTH_SHORT)
//                .show();
        // we will get jasson array list here

        ClubRowItem clubRowItemObj = clubRowItemList.get(position);
        String clubId = clubRowItemObj.getClubid();
        String clubName = clubRowItemObj.getClubname();
        String imageUrl = clubRowItemObj.getImageURL();
        Intent intent = new Intent(getActivity(), ClubDetailsListActivity.class);
        intent.putExtra(Constants.CLUB_ID, clubId);
        intent.putExtra(Constants.IMAGE_URL, imageUrl);
        intent.putExtra(Constants.CLUB_NAME, clubName);
        startActivity(intent);

    }


    @Override
    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText == null || newText.trim().isEmpty() || newText.length() <4) {
            resetSearch();
            return false;
        }

        ArrayList<ClubRowItem> filteredValues = new ArrayList<>(clubRowItemList);


        for (ClubRowItem value : clubRowItemList) {
            String removeValue = value.getClubname().toLowerCase()+value.getLocation().toLowerCase();

            if (!removeValue.contains(newText.toLowerCase())) {
                filteredValues.remove(value);
            }

//            if (!value.getClubname().toLowerCase().contains(newText.toLowerCase())) {
//                filteredValues.remove(value);
//            }
//            if (!value.getLocation().toLowerCase().contains(newText.toLowerCase())) {
//                filteredValues.remove(value);
//            }
        }

//        List<String> filteredValues = new ArrayList<String>(mAllValues);
//        for (String value : mAllValues) {
//            if (!value.toLowerCase().contains(newText.toLowerCase())) {
//                filteredValues.remove(value);
//            }
//        }

        mAdapter = new ClubsListAdapter(getActivity(), filteredValues);
        setListAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

//        mAdapter = new ArrayAdapter<>(mContext, R.layout.club_item_list, filteredValues);
//        setListAdapter(mAdapter);


        return false;
    }

    public void resetSearch() {
//        mAdapter = new ArrayAdapter<>(mContext, R.layout.club_item_list, mAllValues);
//        setListAdapter(mAdapter);
        adapter = new ClubsListAdapter(getActivity(), clubRowItemList);
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    public void eventReceived(String message){
        // conver message to list
        if(message != null){

            try{
                JSONObject eventJObjX = new JSONObject(message);
                clubsListJsonArray = eventJObjX.getJSONArray("jsonResponseList");


            }catch (Exception ex){
                ex.printStackTrace();

            }

        }


        getClubList = true;



    }


    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

//    @Override
//    public void onResume() {
//        getclubListFromDatabase();
//        super.onResume();
//    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
//            String strLocation =
//                    DateFormat.getTimeInstance().format(location.getTime()) + "\n" +
//                            "Latitude=" + location.getLatitude() + "\n" +
//                            "Longitude=" + location.getLongitude();
//
//            textAutoUpdateLocation.setText(strLocation);

            latlong = location.getLatitude()+","+location.getLongitude();


            //Toast.makeText(getActivity(), "latlong3: "+latlong, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            // here we go you can see current lat long.
            //Log.e(TAG, "onConnected: " + String.valueOf(mLastLocation.getLatitude()) + ":" + String.valueOf(mLastLocation.getLongitude()));
//            Toast.makeText(getActivity(),
//                    "permission was granted, :)"+"onConnected: " + String.valueOf(mLastLocation.getLatitude()) + ":" + String.valueOf(mLastLocation.getLongitude()),
//                    Toast.LENGTH_LONG).show();
            latlong = String.valueOf(mLastLocation.getLatitude())+","+String.valueOf(mLastLocation.getLongitude());

        }
        getclubListFromDatabase();

    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(),
                            "permission was granted, :)",
                            Toast.LENGTH_LONG).show();

                    try{
                        LocationServices.FusedLocationApi.requestLocationUpdates(
                                mGoogleApiClient, mLocationRequest, this);
                    }catch(SecurityException e){
                        Toast.makeText(getActivity(),
                                "SecurityException:\n" + e.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(),
                            "permission denied, ...:(",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getActivity(),
                "onConnectionSuspended: \n" ,
                Toast.LENGTH_LONG).show();
        getclubListFromDatabase();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getActivity(),
                "onConnectionFailed: \n" + connectionResult.toString(),
                Toast.LENGTH_LONG).show();
        getclubListFromDatabase();
    }


}
