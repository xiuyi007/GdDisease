package com.li.gddisease.dto;

import com.li.gddisease.entity.Disease;

import java.util.Date;

public class DiseaseChosenDto {
    private String place;
    private Date date;
    private int type;
    private int status;


    public DiseaseChosenDto () {}

    public DiseaseChosenDto(String mplace, int mtype, int mstatus)
    {
        this.place = mplace;
        this.type = mtype;
        this.status = mstatus;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
