package com.li.gddisease;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.li.gddisease.dao.DiseaseDao;
import com.li.gddisease.dao.HandleDao;
import com.li.gddisease.dao.UserDao;
import com.li.gddisease.entity.Disease;
import com.li.gddisease.entity.Handle;
import com.li.gddisease.entity.User;

import util.Converters;

@TypeConverters({Converters.class})
@Database(exportSchema = true,
        entities = {User.class, Disease.class, Handle.class}, version = 2
        )
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "mApp.db";
    private static volatile AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static AppDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                AppDatabase.class,
                DB_NAME)
                .createFromAsset("database/mDatabase.db")
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();

//        return Room.databaseBuilder(
//                context,
//                AppDatabase.class,
//                DB_NAME).fallbackToDestructiveMigration().allowMainThreadQueries().build();
    }



    public abstract UserDao userDao();
    public abstract DiseaseDao diseaseDao();
    public abstract HandleDao handleDao();
}
