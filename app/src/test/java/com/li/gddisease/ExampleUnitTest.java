package com.li.gddisease;

import org.junit.Test;

import static org.junit.Assert.*;

import android.util.Log;

import com.li.gddisease.dto.DiseaseChosenDto;

import util.SqlHelper;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void sqlhelper_test_disease_query()
    {
        DiseaseChosenDto dto_null = new DiseaseChosenDto();
        String sql = SqlHelper.disease_sql(dto_null);
        System.out.println(sql);
        dto_null.setPlace("鄞州区");
        sql = SqlHelper.disease_sql(dto_null);
        System.out.println(sql);
        dto_null.setStatus(1);
        sql = SqlHelper.disease_sql(dto_null);
        System.out.println(sql);
        dto_null.setStatus(2);
        sql = SqlHelper.disease_sql(dto_null);
        System.out.println(sql);
    }
}