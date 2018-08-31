package com.application.club.guestlist.bookedTickets;

import java.util.List;

public class BookedTicketModel {

    private static BookedTicketModel bookedTicketModel = new BookedTicketModel( );

    private BookedTicketModel() { }

    /* Static 'instance' method */
    public static BookedTicketModel getInstance( ) {
        return bookedTicketModel;
    }

    public List<TicketRowItem> getBookedTableRowItems() {
        return bookedTableRowItems;
    }

    public void setBookedTableRowItems(List<TicketRowItem> bookedTableRowItems) {
        this.bookedTableRowItems = bookedTableRowItems;
    }

    public List<TicketRowItem> getBookedPassRowItems() {
        return bookedPassRowItems;
    }

    public void setBookedPassRowItems(List<TicketRowItem> bookedPassRowItems) {
        this.bookedPassRowItems = bookedPassRowItems;
    }

    public List<TicketRowItem> getBookedGuestListRowItems() {
        return bookedGuestListRowItems;
    }

    public void setBookedGuestListRowItems(List<TicketRowItem> bookedGuestListRowItems) {
        this.bookedGuestListRowItems = bookedGuestListRowItems;
    }

    private List<TicketRowItem> bookedTableRowItems;
    private List<TicketRowItem> bookedPassRowItems;
    private List<TicketRowItem> bookedGuestListRowItems;

}
