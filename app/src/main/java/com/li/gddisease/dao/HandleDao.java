package com.li.gddisease.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.li.gddisease.entity.Handle;

@Dao
public interface HandleDao {

    @Insert
    Long[] insert(Handle... handle);

    @Query("delete from Handle")
    void deleteAll();

    @Query("update Handle set status = :dStatus where disease_id = :dId")
    int updateDiseasesStatus(int dId, int dStatus);
}
