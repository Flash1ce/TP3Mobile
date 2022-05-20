package com.example.garneau.demo_tp3.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.garneau.demo_tp3.databinding.FragmentHomeBinding;
import com.example.garneau.demo_tp3.model.Location;
import com.example.garneau.demo_tp3.ui.LocationAdapter;

import java.util.List;

public class HomeFragment extends Fragment {

    private LiveData<List<Location>> lstLocations;
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private LocationAdapter locationAdapter;

    /**
     * Est exécuté quand la view est en cours de création.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return la view
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        return root;
    }

    /**
     * Est exécuté quand la view est créé.
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // déclaration, instanciation du RecyclerView et configuration
        binding.rvLocation.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvLocation.setHasFixedSize(true);

        locationAdapter = new LocationAdapter();
        binding.rvLocation.setAdapter(locationAdapter);

        lstLocations = homeViewModel.getAllLocations();
        // méthode onChanged de l'Observer sur la liste
        lstLocations.observe(getActivity(), new Observer<List<Location>>() {
            @Override
            public void onChanged(List<Location> lstLocations) {
                locationAdapter.setLocations(lstLocations);
            }
        });
    }

    /**
     * Est éxécuté quand la view est détruite. met binding=null.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}