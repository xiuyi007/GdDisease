package com.li.gddisease.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.security.PublicKey;

@Entity(indices = {@Index("user_id"), @Index("disease_id")},
        foreignKeys = {
        @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "user_id"),
        @ForeignKey(entity = Disease.class, parentColumns = "id", childColumns = "disease_id")
        })
public class Handle {


    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "user_id")
    public int userId;

    @ColumnInfo(name = "disease_id")
    public int diseaseId;

    public int status;

    public Handle() {}

    public Handle(int userId, int diseaseId, int status) {
        this.userId = userId;
        this.diseaseId = diseaseId;
        this.status = status;
    }

    @Deprecated
    public Handle(int id, int userId, int diseaseId, int status) {
        this.id = id;
        this.userId = userId;
        this.diseaseId = diseaseId;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(int diseaseId) {
        this.diseaseId = diseaseId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }




}
