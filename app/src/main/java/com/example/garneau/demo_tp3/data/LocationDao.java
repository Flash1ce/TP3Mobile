package com.example.garneau.demo_tp3.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.example.garneau.demo_tp3.model.Location;

/**
 * Interface CRUD de l'object Location.
 * Query getLocation et getAllLocations utilise les liveData.
 */
@Dao
public interface LocationDao {
    //CRUD
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Location location);

    @Update
    void update(Location location);

    @Query("DELETE FROM location_table")
    void deleteAll();

    @Query("SELECT COUNT(id) FROM location_table")
    int getDataCount();

    @Delete
    void deleteALoc(Location property);

    @Query("SELECT * FROM location_table WHERE `id` = :id")
    LiveData<Location> getLocation(int id);

    @Query("SELECT * FROM location_table ORDER BY id DESC")
    LiveData<List<Location>> getAllLocations();

}
