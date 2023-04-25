package com.li.gddisease.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.sql.Date;


@Entity
public class Disease {
    public Disease() {
    }


    @PrimaryKey(autoGenerate = true)
    public int id;


    public String place;
    public double longitude;
    public double latitude;
    public int type;
    public String description;
    public Date date;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    /*
    自动编号后不需要赋值id
     */
    @Deprecated
    public Disease(int id, String place, double longitude, double latitude, int type, String description, Date date) {
        this.id = id;
        this.place = place;
        this.longitude = longitude;
        this.latitude = latitude;
        this.type = type;
        this.description = description;
        this.date = date;
    }

    public Disease(String place, double longitude, double latitude, int type, String description, Date date) {
        this.place = place;
        this.longitude = longitude;
        this.latitude = latitude;
        this.type = type;
        this.description = description;
        this.date = date;
    }
}
