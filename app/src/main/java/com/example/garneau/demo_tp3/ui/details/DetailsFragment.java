package com.example.garneau.demo_tp3.ui.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.garneau.demo_tp3.databinding.DetailsFragmentBinding;
import com.example.garneau.demo_tp3.utils.Categorie;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.example.garneau.demo_tp3.R;
import com.example.garneau.demo_tp3.model.Location;

public class DetailsFragment extends Fragment implements OnMapReadyCallback {

    private DetailsViewModel detailsViewModel;
    private int id;
    private LiveData<Location> location;
    private GoogleMap mMap;
    private DetailsFragmentBinding binding;

    public static DetailsFragment newInstance() {
        return new DetailsFragment();
    }

    /**
     * Est exécuté quand la view est en création
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DetailsFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        detailsViewModel = new ViewModelProvider(this).get(DetailsViewModel.class);


        return root;
    }


    /**
     * Est exécuter quand la view est créé
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((Toolbar) getActivity().findViewById(R.id.toolbar)).setTitle(R.string.menu_detail);

        id = DetailsFragmentArgs.fromBundle(getArguments()).getId();
        assert getArguments() != null;

        ImageView ivImage = view.findViewById(R.id.iv_location_bottom);
        TextView tvNom = view.findViewById(R.id.tv_nom_details);
        TextView tvId = view.findViewById(R.id.tv_id);
        TextView tvCategorie = view.findViewById(R.id.tv_categorie_details);
        TextView tvAdresse = view.findViewById(R.id.tv_adresse_details);

        // méthode onChanged de l'Observer sur le point et passage des valeurs  à la View
        location = detailsViewModel.getLocationById(id);
        location.observe(getActivity(), new Observer<Location>() {
            @Override
            // Paramètre : dernière version observée de la liste de locations
            public void onChanged(Location location) {
                Categorie categorie = location.getCategorie();
                ivImage.setImageResource(categorie.getImageCategorie());
                tvNom.setText(location.getNom());
                tvId.setText(Integer.toString(id));
                tvCategorie.setText(categorie.toString());
                tvAdresse.setText(location.getAdresse());
            }
        });

        // get mapFragment
        SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

    }

    /**
     * Callback quand la map est prête.
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // observer sur la location
        // Paramètre : dernière version observée de la liste de locations
        location.observe(getActivity(), location -> {
            LatLng markerLocation = new LatLng(location.getLatitude(), location.getLongitude());

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(markerLocation)
                    .title(location.getNom())
            );
            marker.setTag(location);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLocation, 13));
        });
    }
}