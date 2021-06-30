package com.example.searchevent.ui.main;

public class Eventlist {
    String event;
    String date;
    String location;

    public Eventlist(String event, String date, String location) {
        this.event = event;
        this.date = date;
        this.location = location;
    }

    public String getEvent() {
        return event;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }
}
