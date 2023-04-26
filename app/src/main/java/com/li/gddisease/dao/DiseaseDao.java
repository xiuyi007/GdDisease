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

    @Query("select disease.id, place, longitude, latitude, type, description, date from disease, handle " +
            "where Disease.id = handle.disease_id and user_id = :userId and status = :status")
    List<Disease> getDisease_user_status(int userId, int status);

    @Insert
    Long[] insertDiseases(Disease... diseases);


    @Insert
    Long insertDisease(Disease diseases);

    @Query("delete from Disease")
    void deleteAll();
}
