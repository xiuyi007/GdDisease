package com.li.gddisease.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.li.gddisease.entity.Disease;
import com.li.gddisease.entity.User;
import com.li.gddisease.pojo.DiseaseReturnPojo;

import java.util.List;

@Dao
public interface DiseaseDao {

    @RawQuery
    List<DiseaseReturnPojo> getDiseaseByConditional(SupportSQLiteQuery query);

    @Query("select * from disease")
    List<Disease> getDiseaseAll();

    @Query("select * from disease where place = :place")
    List<Disease> getDiseaseByPlace(String place);

    @Insert
    void insertDiseases(Disease... diseases);

    @Query("delete from Disease")
    void deleteAll();
}
