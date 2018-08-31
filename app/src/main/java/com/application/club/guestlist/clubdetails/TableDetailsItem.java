package com.application.club.guestlist.clubdetails;

/**
 * Created by vichi on 03/12/17.
 */

public class TableDetailsItem {

    private String clubid;
    private String clubname;
    private String tableid;
    private String details;
    private String tabletype;
    private String size;
    private String cost;
//    private int LocX;
//    private int LocY;
//    private int OHight;
//    private int OWidth;

    private String layoutURL;
    private String eventdate;
    private String booked;

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    private String tableNumber;


    public String getClubid() {
        return clubid;
    }

    public void setClubid(String clubid) {
        this.clubid = clubid;
    }

    public String getClubname() {
        return clubname;
    }

    public void setClubname(String clubname) {
        this.clubname = clubname;
    }

    public String getTableid() {
        return tableid;
    }

    public void setTableid(String tableid) {
        this.tableid = tableid;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTabletype() {
        return tabletype;
    }

    public void setTabletype(String tabletype) {
        this.tabletype = tabletype;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }



    public String getLayoutURL() {
        return layoutURL;
    }

    public void setLayoutURL(String layoutURL) {
        this.layoutURL = layoutURL;
    }

    public String getEventdate() {
        return eventdate;
    }

    public void setEventdate(String eventdate) {
        this.eventdate = eventdate;
    }

    public String getBooked() {
        return booked;
    }

    public void setBooked(String booked) {
        this.booked = booked;
    }




}
