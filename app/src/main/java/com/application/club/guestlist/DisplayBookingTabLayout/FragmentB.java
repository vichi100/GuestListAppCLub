package com.application.club.guestlist.DisplayBookingTabLayout;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.club.guestlist.R;
import com.application.club.guestlist.reports.BookingTableData;
import com.application.club.guestlist.utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentB extends Fragment {

    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment, container, false);
        return rootView;

        /**The below code was when the ListView was used in place of RecyclerView. **/

        /*View view = inflater.inflate(R.layout.fragment_list, container, false);

        list = (ListView) view.findViewById(R.id.list);
        ArrayList stringList= new ArrayList();

        stringList.add("Item 2A");
        stringList.add("Item 2B");
        stringList.add("Item 2C");
        stringList.add("Item 2D");
        stringList.add("Item 2E");
        stringList.add("Item 2F");
        stringList.add("Item 2G");
        stringList.add("Item 2H");
        stringList.add("Item 2I");
        stringList.add("Item 2J");
        stringList.add("Item 2K");
        stringList.add("Item 2L");
        stringList.add("Item 2M");
        stringList.add("Item 2N");
        stringList.add("Item 2O");
        stringList.add("Item 2P");
        stringList.add("Item 2Q");
        stringList.add("Item 2R");
        stringList.add("Item 2S");
        stringList.add("Item 2T");
        stringList.add("Item 2U");
        stringList.add("Item 2V");
        stringList.add("Item 2W");
        stringList.add("Item 2X");
        stringList.add("Item 2Y");
        stringList.add("Item 2Z");

        CustomAdapter adapter = new CustomAdapter(stringList,getActivity());
        list.setAdapter(adapter);

        return view;*/
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        try {
            super.onViewCreated(view, savedInstanceState);
            List<BookingDataModel> bookingDataModelList = new ArrayList<>();

            BookingTableData bookingTableData = BookingTableData.getInstance();
            JSONArray bookedTableDetailsJArray = bookingTableData.getBookedPassDetailsJArray();
            for (int i = 0; i < bookedTableDetailsJArray.length(); i++) {
                JSONObject jobj = bookedTableDetailsJArray.getJSONObject(i);
                BookingDataModel bdm = new BookingDataModel();

                String customerName  = jobj.getString(Constants.CUSTOMERNAME);
                String QRNumber = jobj.getString(Constants.QRNUMBER);
                String eventDate = jobj.getString(Constants.EVENT_DATE);
                String costStr = jobj.getString(Constants.COSTAFTERDISCOUNT);
                String paidAmt =  jobj.getString(Constants.PAID_AMOUNT);
                String remainningAmt =  jobj.getString(Constants.REMAINING_AMOUNT);
                String ticketDetails = jobj.getString(Constants.TICKET_DETAILS);

                bdm.setCutomername(customerName);
                bdm.setQRnumber(QRNumber);
                bdm.setEventDate(eventDate);
                bdm.setCost(costStr);
                bdm.setPaidAmount(paidAmt);
                bdm.setRemainingAmount(remainningAmt);
                bdm.setTicketDetails(ticketDetails);
                bookingDataModelList.add(bdm);
            }

            //String[] items = getResources().getStringArray(R.array.tab_A);
            BookingDataAdapter adapter = new BookingDataAdapter(bookingDataModelList);
            //String[] items = getResources().getStringArray(R.array.tab_B);
            //RecyclerViewAdapter adapter = new RecyclerViewAdapter(items);
            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

    }catch(Exception ex){
        ex.printStackTrace();
    }
    }
}