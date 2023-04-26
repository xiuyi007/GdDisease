package com.li.gddisease.pojo;

import com.li.gddisease.entity.Disease;

import java.sql.Date;

public class DiseaseReturnPojo extends Disease {

    private int status;

    public DiseaseReturnPojo() {}
    @Deprecated
    public DiseaseReturnPojo(int id, String place, double longitude, double latitude, int type, String description, Date date, int status) {
        super(id, place, longitude, latitude, type, description, date);
        this.status = status;
    }

    @Deprecated
    public DiseaseReturnPojo(String place, double longitude, double latitude, int type, String description, Date date, int status) {
        super(place, longitude, latitude, type, description, date);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
}
