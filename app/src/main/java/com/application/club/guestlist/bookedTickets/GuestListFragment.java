package com.application.club.guestlist.bookedTickets;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.application.club.guestlist.R;

import java.util.List;


/* Fragment used as page 2 */
public class GuestListFragment extends ListFragment {

    TicketRowAdapter adapter;
    BookedTicketModel bookedTicketModel =  BookedTicketModel.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.guestlist_list_fragment, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        List<TicketRowItem> bookedTableRowItems = bookedTicketModel.getBookedGuestListRowItems();

        adapter = new TicketRowAdapter(getActivity(), bookedTableRowItems);
        setListAdapter(adapter);

        TextView totaltablebookedtv = (TextView) getActivity().findViewById(R.id.totalbooked);
        totaltablebookedtv.setText(Integer.toString(bookedTableRowItems.size()));
    }

}
