package com.application.club.guestlist.reports;

import org.json.JSONArray;

public class BookingTableData {

    private static BookingTableData bookingTableData = null;

    // variable of type String
    public String s;

    // private constructor restricted to this class itself
    private BookingTableData()
    {
        s = "Hello I am a string part of Singleton class";
    }

    // static method to create instance of Singleton class
    public static BookingTableData getInstance()
    {
        if (bookingTableData == null)
            bookingTableData = new BookingTableData();

        return bookingTableData;
    }

    public JSONArray getBookedTableDetailsJArray() {
        return bookedTableDetailsJArray;
    }

    public void setBookedTableDetailsJArray(JSONArray bookedTableDetailsJArray) {
        this.bookedTableDetailsJArray = bookedTableDetailsJArray;
    }

    public JSONArray getBookedGuestlistDetailsJArray() {
        return bookedGuestlistDetailsJArray;
    }

    public void setBookedGuestlistDetailsJArray(JSONArray bookedGuestlistDetailsJArray) {
        this.bookedGuestlistDetailsJArray = bookedGuestlistDetailsJArray;
    }

    public JSONArray getBookedPassDetailsJArray() {
        return bookedPassDetailsJArray;
    }

    public void setBookedPassDetailsJArray(JSONArray bookedPassDetailsJArray) {
        this.bookedPassDetailsJArray = bookedPassDetailsJArray;
    }

    JSONArray bookedTableDetailsJArray;
    JSONArray bookedGuestlistDetailsJArray;
    JSONArray bookedPassDetailsJArray;

}
