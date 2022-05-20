package com.example.garneau.demo_tp3.utils;


import com.example.garneau.demo_tp3.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Enum: Object des catégorie qui existe.
 */
public enum Categorie {
    MARCHE("Marche"),
    SKI("Ski"),
    RAQUETTE("Raquette"),
    CAMPING("Camping");

    private String strVal;

    /**
     * Constructeur de Categorie.
     * @param toString
     */
    Categorie(String toString) {
        strVal = toString;
    }

    /**
     * toString de Categorie.
     * @return
     */
    @Override
    public String toString() {
        return strVal;
    }

    /**
     * Permet d'obtenir l'image en fonction de la catégorie.
     * @return return l'image. R.drawable (un int)
     */
    public int getImageCategorie(){
        switch (this) {
            case CAMPING:
                return R.drawable.camping;
            case RAQUETTE:
                return R.drawable.raquette;
            case MARCHE:
                return R.drawable.marche;
            case SKI:
                return R.drawable.ski;
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }

    /**
     * Permet d'obtenir la liste de tous les catégories en string.
     * @return retourne liste de catégorie en string.
     */
    public static List<String> getAllCategories() {
        List<String> lstCat = new ArrayList<>();
        lstCat.add(Categorie.MARCHE.toString());
        lstCat.add(Categorie.SKI.toString());
        lstCat.add(Categorie.RAQUETTE.toString());
        lstCat.add(Categorie.CAMPING.toString());
        return lstCat;
    }

}
