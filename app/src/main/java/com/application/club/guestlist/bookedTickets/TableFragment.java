package com.application.club.guestlist.bookedTickets;


import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.application.club.guestlist.R;
import com.application.club.guestlist.bookedPasses.BookedPassesListAdapter;
import com.application.club.guestlist.bookedPasses.PassRowItem;
import com.application.club.guestlist.service.EventListener;
import com.application.club.guestlist.service.SocketOperator;
import com.application.club.guestlist.utils.Constants;
import com.application.club.guestlist.utils.UtillMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/* Fragment used as page 1 */
public class TableFragment extends ListFragment implements EventListener {


    private List<TicketRowItem> bookedTableRowItems;
    private List<TicketRowItem> bookedPassRowItems;
    private List<TicketRowItem> bookedGuestListRowItems;
    static JSONArray bookedTicketListJsonArray;

    BookedTicketModel bookedTicketModel =  BookedTicketModel.getInstance();

    boolean getTicketList = false;

    SocketOperator socketOperator  = new SocketOperator(this);

    TicketRowAdapter adapter;
    private List<TicketRowItem> rowItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.table_list_fragment, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        try{
            SharedPreferences settings = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
            String clubid = settings.getString("clubid","");
            String eventDate = UtillMethods.getTodayDate();
            JSONObject getbookedTicketFromDatabase = new JSONObject();
            getbookedTicketFromDatabase.put("action", "getbookedTicketFromDatabase");
            getbookedTicketFromDatabase.put(Constants.EVENT_DATE, eventDate);
            getbookedTicketFromDatabase.put(Constants.CUSTOMERID, clubid );

            socketOperator.sendMessage(getbookedTicketFromDatabase);

            while(!getTicketList){
                SystemClock.sleep(1000);
            }

        }catch (Exception ex){

        }


        bookedTableRowItems = new ArrayList<TicketRowItem>();
        bookedPassRowItems = new ArrayList<TicketRowItem>();
        bookedGuestListRowItems = new ArrayList<TicketRowItem>();

        try{

            if(bookedTicketListJsonArray != null){
                for(int i=0; i < bookedTicketListJsonArray.length(); i++){
                    TicketRowItem bookedTicketRowObj = new TicketRowItem();

                    JSONObject bookedTicketjObj = bookedTicketListJsonArray.getJSONObject(i);

                    String clubid = bookedTicketjObj.getString(Constants.CLUB_ID);
                    String clubname = bookedTicketjObj.getString(Constants.CLUB_NAME);
                    String cutomername = bookedTicketjObj.getString(Constants.CUSTOMERNAME);
                    String mobile = bookedTicketjObj.getString(Constants.MOBILE);
                    String customerId = bookedTicketjObj.getString(Constants.CUSTOMERID);
                    String QRnumber = bookedTicketjObj.getString(Constants.QRNUMBER);
                    String tickettype = bookedTicketjObj.getString(Constants.TICKETTYPE);
                    String eventDate = bookedTicketjObj.getString(Constants.EVENT_DATE);
                    String cost = bookedTicketjObj.getString(Constants.COSTAFTERDISCOUNT);
                    String remainingAmt = bookedTicketjObj.getString(Constants.REMAINING_AMOUNT);
                    String ticketDetails = bookedTicketjObj.getString(Constants.TICKET_DETAILS);
                    bookedTicketRowObj.setClubid(clubid);
                    bookedTicketRowObj.setClubname(clubname);
                    bookedTicketRowObj.setCutomername(cutomername);
                    bookedTicketRowObj.setMobile(mobile);
                    bookedTicketRowObj.setCustomerId(customerId);
                    bookedTicketRowObj.setQRnumber(QRnumber);
                    bookedTicketRowObj.setTickettype(tickettype);
                    bookedTicketRowObj.setEventDate(eventDate);
                    bookedTicketRowObj.setCost(cost);
                    bookedTicketRowObj.setRemainingAmount(remainingAmt);
                    bookedTicketRowObj.setTicketDetails(ticketDetails);

                    if(tickettype != null && tickettype.equalsIgnoreCase("table")){

                        bookedTableRowItems.add(bookedTicketRowObj);
                    }else if(tickettype != null && tickettype.equalsIgnoreCase("pass")){

                        bookedPassRowItems.add(bookedTicketRowObj);
                    }else if(tickettype != null && tickettype.contains("guest")){

                        bookedGuestListRowItems.add(bookedTicketRowObj);
                    }

                }

            }

        }catch (Exception ex){

        }


        bookedTicketModel.setBookedPassRowItems(bookedPassRowItems);
        bookedTicketModel.setBookedGuestListRowItems(bookedGuestListRowItems);


        adapter = new TicketRowAdapter(getActivity(), bookedTableRowItems);
        setListAdapter(adapter);



        TextView totaltablebookedtv = (TextView) getActivity().findViewById(R.id.totalbooked);
        totaltablebookedtv.setText(Integer.toString(bookedTableRowItems.size()));

        //getListView().setOnItemClickListener(this);

    }


    public void eventReceived(String message){
        // conver message to list
        if(message != null){

            try{
                JSONObject eventJObjX = new JSONObject(message);
                bookedTicketListJsonArray = eventJObjX.getJSONArray("bookedTicketList");



            }catch (Exception ex){
                ex.printStackTrace();

            }

        }


        getTicketList = true;



    }
}
