package com.li.gddisease.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
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

    @Insert
    void insertUsers(Disease... diseases);
}
