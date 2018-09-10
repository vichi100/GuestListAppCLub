package com.application.club.guestlist.DisplayBookingTabLayout;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.application.club.guestlist.R;

import java.util.List;

public class BookingDataAdapter extends RecyclerView.Adapter<BookingDataAdapter.BookingDataViewHolder> {

    private List<BookingDataModel> moviesList;

    public class BookingDataViewHolder extends RecyclerView.ViewHolder {
        public TextView qrNumber, eventDate, description, costtv, paidamttv, remainingamttv;

        public BookingDataViewHolder(View view) {
            super(view);
            qrNumber = (TextView) view.findViewById(R.id.qrNumber);
            eventDate = (TextView) view.findViewById(R.id.eventDate);
            description = (TextView) view.findViewById(R.id.description);
            costtv = (TextView) view.findViewById(R.id.costv);
            paidamttv = (TextView) view.findViewById(R.id.paidamtv);
            remainingamttv = (TextView) view.findViewById(R.id.remainingamtv);
        }
    }

    public BookingDataAdapter(List<BookingDataModel> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public BookingDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ticket_list_item, parent, false);

        return new BookingDataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookingDataViewHolder holder, int position) {
        BookingDataModel movie = moviesList.get(position);
        holder.qrNumber.setText(movie.getQRnumber());
        holder.eventDate.setText(movie.getEventDate());
        holder.description.setText(movie.getTicketDetails());
        holder.costtv.setText("Cost : "+movie.getCost()+" | ");
        holder.paidamttv.setText("Paid : "+movie.getPaidAmount()+" | ");
        holder.remainingamttv.setText("Remaining : "+movie.getRemainingAmount());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

}
