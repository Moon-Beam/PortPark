package com.timilehin.portpark.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CarPark {
    @SerializedName("geometry")
    private Geometry geometry;
    public Geometry getGeometry() { return this.geometry; }

    @SerializedName("id")
    private String id;
    public String getId() { return this.id; }

    @SerializedName("name")
    private String name;
    public String getName() { return this.name; }

    @SerializedName("opening_hours")
    private OpeningHours opening_hours;
    public OpeningHours getOpeningHours() { return this.opening_hours; }

    @SerializedName("place_id")
    private String place_id;
    public String getPlaceId() { return this.place_id; }

    @SerializedName("rating")
    private double rating;
    public double getRating() { return this.rating; }

    @SerializedName("reference")
    private String reference;
    public String getReference() { return this.reference; }

    @SerializedName("vicinity")
    private String vicinity;
    public String getVicinity() { return this.vicinity; }

}
