package com.example.searchevent;

public class Favourite {
    String id;
    String name;
    String venue;
    String date;
    String category;
    String artist;

    public Favourite(String id, String name, String venue, String date, String category, String artist){
        this.id = id;
        this.name = name;
        this.venue = venue;
        this.date = date;
        this.category = category;
        this.artist = artist;
    }

    public String getID(){
        return id;
    }
    public String getEvent(){
        return name;
    }
    public String getVenue(){
        return venue;
    }
    public  String getDate(){
        return date;
    }
    public String getCategory(){
        return category;
    }
    public String getArtist(){
        return  artist;
    }
    public void setID(String id){
        this.id = id;
    }
    public void setEvent(String name){
        this.name = name;
    }
    public void setVenue(String venue){
        this.venue = venue;
    }
    public void  setDate(String date){
        this.date = date;
    }
}

