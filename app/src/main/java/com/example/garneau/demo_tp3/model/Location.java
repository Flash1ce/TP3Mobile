package com.example.garneau.demo_tp3.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.garneau.demo_tp3.utils.Categorie;

/**
 * Modèle de BD de l'object l'ocation.
 */
@Entity(tableName = "location_table")
public class Location {

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @NonNull
    @ColumnInfo(name = "name_col")
    public String nom;
    public String categorie;
    public String adresse;
    public Double latitude;
    public Double longitude;

    /**
     * Constructeur de l'objet location. Prend en paramètre ces data.
     * @param nom Nom de la location.
     * @param categorie Catégorie de la location. Est en rapport avec la class Categorie.
     * @param adresse Adresse de la location.
     * @param latitude La latitude de la position de la location.
     * @param longitude La longitude de la position de la location.
     */
    public Location(String nom, String categorie, String adresse, Double latitude, Double longitude) {
        this.nom = nom;
        this.categorie = categorie;
        this.adresse = adresse;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Permet d'obtenir l'id de la location.
     * @return Integer id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Permet de set l'id de la location.
     * @param id Integer id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Permet d'obtenir le nom de la location.
     * @return String Nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Permet de set le nom de la location.
     * @param nom String nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Permet d'obtenir la catégorie de la location. (l'object Categorie)
     * @return Categorie categorie
     */
    public Categorie getCategorie() {
        if (categorie.equals(Categorie.CAMPING.toString())) return Categorie.CAMPING;
        else if (categorie.equals(Categorie.RAQUETTE.toString())) return Categorie.RAQUETTE;
        else if (categorie.equals(Categorie.MARCHE.toString())) return Categorie.MARCHE;
        else return Categorie.SKI;
    }

    /**
     * Permet de set la catégorie.
     * @param categorie String catégorie
     */
    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    /**
     * Permet d'obtenir l'adresse de la location
     * @return String adresse
     */
    public String getAdresse() {
        return adresse;
    }

    /**
     * Permet de set l'adresse.
     * @param adresse String adresse
     */
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    /**
     * Permet d'obtenir la latitude
     * @return Double latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * Permet de set la latitude
     * @param latitude Double latitude
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * Permet d'obtenir la longitude.
     * @return Double longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * Permet de set la longitude.
     * @param longitude Double longitude
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
