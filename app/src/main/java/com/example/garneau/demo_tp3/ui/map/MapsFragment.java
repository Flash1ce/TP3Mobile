package com.example.garneau.demo_tp3.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.garneau.demo_tp3.R;
import com.example.garneau.demo_tp3.databinding.FragmentMapsBinding;
import com.example.garneau.demo_tp3.utils.Categorie;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    public static final int LOCATION_PERMISSION_CODE = 1;
    private static final String TAG = "tag";
    private Boolean isMarkerOpen = false;
    public FloatingActionButton fab;
    private FragmentMapsBinding binding;
    private Boolean addLocation = false;
    private MapsViewModel mapsViewModel;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private Location locationUser;
    private TextView id_distance;
    private LatLng userLoc;

    /**
     * Est éxécuté quand la view est entrain d'être créé.
     * Instanciation du binding, du viewModel et du fab.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState Représente le Bundle
     * @return Retourne la view root.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // instanciation du binding / viewModel / fab
        binding = FragmentMapsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mapsViewModel = new ViewModelProvider(this).get(MapsViewModel.class);

        fab = root.findViewById(R.id.fab);
        id_distance = root.findViewById(R.id.distance);

        if (savedInstanceState != null) {
            addLocation = !savedInstanceState.getBoolean("fabOn", false);
            actionFab();
        }

        return root;
    }

    /**
     * Est exécuté quand la view est créée.
     *
     * @param view               Est la view
     * @param savedInstanceState Est un Bundle
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Récupération du mapFragment.
        SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        // Vérification si la permission est accordé sinon lancement de la demande pour la géolocalisation.
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
        }

        // Listener de click sur le fab pour basculer entre les modes.
        fab.setOnClickListener(view1 -> actionFab());
    }

    /**
     * Callback quand la map est prète à être utilisé. L'ajout de markers, de lignes, de listeners
     * ou bouger la caméras ce fait ici.
     *
     * @param googleMap la map de google.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true); // Zoom controleur

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLastLocation();
        }

        // Récupération et ajouts de tous les markers.
        LiveData<List<com.example.garneau.demo_tp3.model.Location>> lstLiveLocations = mapsViewModel.getAllLocations();
        lstLiveLocations.observe(getActivity(), locations -> {
            for (com.example.garneau.demo_tp3.model.Location location : locations) {
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .title(location.getNom())
                        .position(new LatLng(location.getLatitude(), location.getLongitude()))
                );
                marker.setTag(location);
            }
        });

        // Listener des click sur la map.
        mMap.setOnMapClickListener(latLng -> {
            if (addLocation == true && !isMarkerOpen) {
                MapsFragment.this.actionClicCarte(new LatLng(latLng.latitude, latLng.longitude));
            }
            if (isMarkerOpen) {
                isMarkerOpen = false; // le marker est fermé.
                id_distance.setText(null); // Retire la distance : aucun point est sélectioner.
            }
        });

        // Configuration du Layout pour les popups (InfoWindow). Dialogue des markers.
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                showDistance(marker); // Affichage de distance sur le fragment

                // Déployer le layout de la vue Marker et passer les valeurs du point cliqué afin d'affichage
                if (!marker.getTitle().equals(getString(R.string.ici))){
                    View markerView = getActivity().getLayoutInflater().inflate(R.layout.marker_layout, null);
                    ImageView ivImage = markerView.findViewById(R.id.iv_photo_map);
                    TextView tvNom = markerView.findViewById(R.id.tv_nom_map);
                    TextView tvCat = markerView.findViewById(R.id.tv_cat_map);
                    TextView tvAdr = markerView.findViewById(R.id.tv_adr_map);


                    com.example.garneau.demo_tp3.model.Location location = (com.example.garneau.demo_tp3.model.Location) marker.getTag();
                    ivImage.setImageResource(location.getCategorie().getImageCategorie());
                    tvNom.setText(location.getNom());
                    tvCat.setText(location.getCategorie().toString());
                    tvAdr.setText(location.getAdresse());

                    isMarkerOpen = true;

                    return markerView;
                }
                isMarkerOpen = true;

                return null;
            }
        });

    }

    /**
     * Quand l'activité est détruite, onSaveInstanceState
     * permet de sauvegarder l'états du fab.
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        // insertion de la liste client dans le Bundle et du solde
        outState.putBoolean("fabOn", addLocation);
        super.onSaveInstanceState(outState);
    }

    /**---------------------------------------UTILS ----------------------------------------------*/

    /**
     * Action quand le fab est cliqué. Changement du mode et modification de la couleur.
     */
    @SuppressLint("ResourceAsColor")
    private void actionFab() {
        if (!addLocation) {
            fab.setBackgroundTintList(AppCompatResources.getColorStateList(getActivity(), R.color.green));
            addLocation = true;
        } else {
            addLocation = false;
            fab.setBackgroundTintList(AppCompatResources.getColorStateList(getActivity(), R.color.red));
        }
    }

    /**
     * Permet de vérifier si la permission de la géolocalisation est autorisé, si oui le marker est positionné.
     */
    public void getLastLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
        } else {
            // Instance du service de localisation
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
            // Définir la dernière localisation connue de l'utilisateur
            fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
                if (location != null) {
                    locationUser = location;
                    userLoc = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(userLoc).title(getString(R.string.ici)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLoc, 13));
                }
            });
        }
    }

    /**
     * Si la permission est ok
     *
     * @param requestCode
     * @param permissions  La permission
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Log.d(TAG, "onRequestPermissionsResult: ");
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(requireActivity());
                    dialog.setTitle(R.string.permReq);
                    dialog.setMessage(R.string.permImporGeo);
                    dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{
                                    Manifest.permission.ACCESS_FINE_LOCATION
                            }, LOCATION_PERMISSION_CODE);
                        }
                    });
                    dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(requireActivity(), R.string.impoLocal, Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();
                }
            }
        }
    }

    /**
     * Permet de calculer la distance entre la position de l'utilisateur et le marker.
     *
     * @param mMarkerA le marker sélectionné
     */
    private void showDistance(Marker mMarkerA) {
        Location markerLoc = new Location("MarkerMessage");
        markerLoc.setLatitude(mMarkerA.getPosition().latitude);
        markerLoc.setLongitude(mMarkerA.getPosition().longitude);

        // Validation que l'utilisateur a bien une location.
        // Permet d'éviter les crash si l'émulateur bug et ne met pas la localisation de l'utilisateur
        if (locationUser != null) {
            // obtention de la valeur de distance en km
            float distance = (markerLoc.distanceTo(locationUser)/1000);
            id_distance.setText(Float.toString(distance));
        } else{
            id_distance.setText(Float.toString(0));
        }
    }

    /**
     * Permet de récupérer l'adresse d'une location avec la latitude et la longitude.
     * @param p_latLng (object) latitude et longitude
     * @return Le string de l'adresse
     */
    private String findAdresse(LatLng p_latLng) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> address = geocoder.getFromLocation(p_latLng.latitude, p_latLng.longitude, 1);
            return address.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Popup dialog sur un clic sur la carte afin d'ajouter un point dans la BD.
     * @param p_latLng latitude et longitude (position du click sur la map.)
     */
    private void actionClicCarte(LatLng p_latLng) {
        View setView = getLayoutInflater().inflate(R.layout.set_location_dialog, null);

        // on définit dynamiquement un LinearLayout
        LinearLayout layout = new LinearLayout(setView.getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText etNom = new EditText(setView.getContext());
        etNom.setHint("Nom");
        layout.addView(etNom);

        Spinner spinner = new Spinner(getActivity());
        spinner.setAdapter(new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                Categorie.getAllCategories())
        );

        layout.addView(spinner);

        // Création de la location
        com.example.garneau.demo_tp3.model.Location location =
                new com.example.garneau.demo_tp3.model.Location(
                        null,
                        null,
                        findAdresse(p_latLng),
                        p_latLng.latitude,
                        p_latLng.longitude
                );


        // Création d'un objet permettant de gérer l'événement sur le bouton "OK" dans l'AlertDialog
        BtnSetHandler setHandler = new BtnSetHandler(etNom, spinner, location);

        // AlertDialog ajouts marker.
        new android.app.AlertDialog.Builder(getContext())
                .setTitle(R.string.menu_addPinne)
                .setView(layout)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.ok, setHandler)
                .show();
    }

    /**
     * Méthode asynchrone d'insertion dans la BD
     */
    private static class InsertLocationDbAsync extends AsyncTask<Void, Void, Void> {
        private final MapsViewModel mapsViewModel;
        private final com.example.garneau.demo_tp3.model.Location location;


        public InsertLocationDbAsync(MapsViewModel mvm, com.example.garneau.demo_tp3.model.Location p_location) {
            this.mapsViewModel = mvm;
            this.location = p_location;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mapsViewModel.addLocation(location);
            return null;
        }
    }

    /**
     * Classe interne pour gérer le clic sur le bouton "OK" du pop-up de modification.
     * Hérite de DialogInterface.OnClickListener
     */
    private class BtnSetHandler implements DialogInterface.OnClickListener {

        private final EditText setTv1;
        private final Spinner spinner;
        private final com.example.garneau.demo_tp3.model.Location location;

        public BtnSetHandler(EditText tv1_txtSet, Spinner spinner, com.example.garneau.demo_tp3.model.Location p_location) {
            this.setTv1 = tv1_txtSet;
            this.spinner = spinner;
            this.location = p_location;
        }

        @Override
        public void onClick(DialogInterface p_dialog, int p_which) {
            // construction du point
            this.location.setCategorie(this.spinner.getSelectedItem().toString());
            this.location.setNom(this.setTv1.getText().toString());

            // Ajouts du point dans la bd.
            InsertLocationDbAsync insertLocationDbAsync = new InsertLocationDbAsync(mapsViewModel, location);
            insertLocationDbAsync.doInBackground();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()),
                    13));

        }
    }
}
