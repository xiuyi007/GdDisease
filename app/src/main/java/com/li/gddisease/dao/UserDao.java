package com.li.gddisease.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.li.gddisease.entity.User;

@Dao
public interface UserDao {
    @Query("select * from user")
    List<User> getALL();

    @Query("select * from user where id = :id")
    User getUserById(int id);


    @Delete
    int delete(User... users);

    @Insert
    Long[] insertUsers(User... users);

    @Query("select * from user where username = :uname")
    User getUserByName(String uname);


    @Update
    public int updateUsers(User... users);

    /*    @Query("select * from user where username in (:name)")
    List<User> getUserTest(String[] name);*/
}
