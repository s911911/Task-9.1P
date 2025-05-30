package com.example.task71p;

public class Item {
    private int    id;
    private String postType;
    private String personName;
    private String phone;
    private String description;
    private String date;
    private String location;
    private double latitude;
    private double longitude;


    public Item(int id, String postType, String personName, String phone,
                String description, String date, String location, double latitude, double longitude) {
        this.id          = id;
        this.postType    = postType;
        this.personName  = personName;
        this.phone       = phone;
        this.description = description;
        this.date        = date;
        this.location    = location;
        this.latitude    = latitude;
        this.longitude   = longitude;
    }

    public int    getId()          { return id; }
    public String getPostType()    { return postType; }
    public String getPersonName()  { return personName; }
    public String getPhone()       { return phone; }
    public String getDescription() { return description; }
    public String getDate()        { return date; }
    public String getLocation()    { return location; }
    public double getLatitude()    { return latitude; }
    public double getLongitude()   { return longitude; }
}

