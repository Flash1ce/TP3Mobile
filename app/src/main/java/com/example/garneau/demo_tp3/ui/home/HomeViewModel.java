package com.example.garneau.demo_tp3.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.garneau.demo_tp3.data.LocationRoomDatabase;
import com.example.garneau.demo_tp3.model.Location;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private final LiveData<List<Location>> lstLocations;
    private final LocationRoomDatabase mDb;

    /**
     * Constructeur du HomeViewModel. Récupère la liste des locations dans la bd.
     *
     * @param application
     */
    public HomeViewModel(Application application) {
        super(application);
        mDb = LocationRoomDatabase.getDatabase(application);
        lstLocations = mDb.LocationDao().getAllLocations();
    }

    /**
     * Permet d'obtenir la liste de tous les points.
     *
     * @return return la liste live data
     */
    public LiveData<List<Location>> getAllLocations() {
        return lstLocations;
    }

}