package com.example.garneau.demo_tp3.ui.map;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.garneau.demo_tp3.data.AppExecutors;
import com.example.garneau.demo_tp3.data.LocationRoomDatabase;
import com.example.garneau.demo_tp3.model.Location;
import com.example.garneau.demo_tp3.utils.Categorie;

import java.util.ArrayList;
import java.util.List;

public class MapsViewModel extends AndroidViewModel {

    private final LiveData<List<Location>> lstLocations;
    private LocationRoomDatabase mDb;

    /**
     * Constructeur du MapsViewModel. Récupère la liste des locations dans la bd.
     *
     * @param application
     */
    public MapsViewModel(Application application) {
        super(application);

        mDb = LocationRoomDatabase.getDatabase(application);
        lstLocations = LocationRoomDatabase.getDatabase(application).LocationDao().getAllLocations();
    }

    /**
     * Permet d'obtenir toutes les locations de la bd.
     *
     * @return liveDate liste des locations
     */
    public LiveData<List<Location>> getAllLocations() {
        return lstLocations;
    }

    /**
     * Permet d'ajouter une location dans la bd.
     *
     * @param location Location à ajouter
     */
    public void addLocation(Location location) {
        mDb = LocationRoomDatabase.getDatabase(getApplication());
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.LocationDao().insert(location);
            }
        });
    }
}