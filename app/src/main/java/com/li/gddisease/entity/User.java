package com.li.gddisease.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity
public class User {
    public User() {}
    public User(int mid, String mUsername, String mPassword)
    {
        id = mid;
        username = mUsername;
        password = mPassword;
    }

    @PrimaryKey
    public int id;

    public String username;

    public String password;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



}
