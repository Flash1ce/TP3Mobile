package com.example.garneau.demo_tp3.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.garneau.demo_tp3.model.Location;


@Database(entities = {Location.class}, version = 2, exportSchema = true)
public abstract class LocationRoomDatabase extends RoomDatabase {
    // abstract, ce n'est pas une classe d'implémentation
    // l'implémentation est fournie par le room

    // Singleton
    public static LocationRoomDatabase INSTANCE;

    // DAO
    public abstract LocationDao LocationDao();

    public static synchronized LocationRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            // Crée la BDD
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    LocationRoomDatabase.class, "todo_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
