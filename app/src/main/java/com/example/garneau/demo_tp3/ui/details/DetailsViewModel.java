package com.example.garneau.demo_tp3.ui.details;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.garneau.demo_tp3.data.LocationRoomDatabase;
import com.example.garneau.demo_tp3.model.Location;

import java.util.List;

public class DetailsViewModel extends AndroidViewModel {

    private LocationRoomDatabase mDb;
    private LiveData<Location> location;

    /**
     * Constructeur
     * @param application
     */
    public DetailsViewModel(Application application) {
        super(application);
        mDb = LocationRoomDatabase.getDatabase(application);
    }

    /**
     * Permet de récupérer la location avec sont id.
     * @param p_id id de la location
     * @return la location
     */
    public LiveData<Location> getLocationById(int p_id){
        location = mDb.LocationDao().getLocation(p_id);
        return location;
    }
}