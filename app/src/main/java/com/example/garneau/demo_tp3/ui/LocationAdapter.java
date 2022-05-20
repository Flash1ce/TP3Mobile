package com.example.garneau.demo_tp3.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.garneau.demo_tp3.databinding.LocationRowBinding;
import com.example.garneau.demo_tp3.model.Location;
import com.example.garneau.demo_tp3.ui.home.HomeFragmentDirections;
import com.example.garneau.demo_tp3.utils.Categorie;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter qui permet d'afficher la location dans la liste sur la page home.
 */
public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationHolder> {
    private LocationRowBinding binding;
    private List<Location> lstLocations = new ArrayList<>();
    private Context context;

    /**
     * Quand la view est en cours de création.
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public LocationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Version de l'inflater prenant une view parente en paramètre (héritée de MainActivity)
        context = parent.getContext();
        binding = LocationRowBinding.inflate(LayoutInflater.from(context), parent, false);
        View view = binding.getRoot();

        return new LocationHolder(view); // Retourne un LocationHolder
    }

    /**
     * Binding avec le holder des éléments contenus dans la view
     *
     * @param holder   le holder
     * @param position position de l'item dans la liste.
     */
    @Override
    public void onBindViewHolder(@NonNull LocationHolder holder, @SuppressLint("RecyclerView") int position) {
        if (lstLocations != null) {
            Location locationCurrent = lstLocations.get(position);

            // passe les bonnes valeurs aux éléments du holder
            holder.tv_addresse.setText(locationCurrent.getAdresse());
            holder.tv_nom.setText(locationCurrent.getNom());
            Categorie categorie = locationCurrent.getCategorie();
            holder.tv_categorie.setText(categorie.toString());
            holder.iv_loc_cat.setImageResource(categorie.getImageCategorie());

            // Navigation vers le fragment Détail avec Id du point détaillé (clique sur rangée)
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavDirections action = (NavDirections) HomeFragmentDirections.actionNavHomeToDetailsFragment(lstLocations.get(position).getId());
                    Navigation.findNavController(view).navigate(action);
                }
            });
        }
    }

    /**
     * Permet d'obtenir le nombre d'item.
     * @return int du nombre d'item.
     */
    @Override
    public int getItemCount() {
        if (lstLocations != null)
            return lstLocations.size();
        else return 0;
    }

    /**
     * Permet de set la liste de locations dans la variable.
     * @param locations List<Location> locations
     */
    public void setLocations(List<Location> locations) {
        lstLocations = locations;
        notifyDataSetChanged();
    }

    /**
     * Référence les éléments de la vue
     */
    class LocationHolder extends RecyclerView.ViewHolder {
        public ImageView iv_loc_cat;
        public TextView tv_categorie;
        public TextView tv_nom;
        public TextView tv_addresse;

        public LocationHolder(View itemView) {
            super(itemView);

            this.iv_loc_cat = binding.ivLocCat;
            this.tv_categorie = binding.tvCategorie;
            this.tv_nom = binding.tvNom;
            this.tv_addresse = binding.tvAdresse;
        }
    }
}