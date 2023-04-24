package com.li.gddisease.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.li.gddisease.entity.Disease;
import com.li.gddisease.entity.User;

import java.util.List;

@Dao
public interface DiseaseDao {

    @RawQuery
    List<Disease> getDiseaseByConditional(SupportSQLiteQuery query);

}
