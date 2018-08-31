package com.application.club.guestlist.DisplayBookingTabLayout;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.application.club.guestlist.R;

public class TextItemViewHolder extends RecyclerView.ViewHolder {
    private TextView textView;


    public TextItemViewHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.list_item);
    }

    public void bind(String text) {
        textView.setText(text);
    }

}
