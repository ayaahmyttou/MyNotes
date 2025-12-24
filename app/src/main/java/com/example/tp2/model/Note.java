package com.example.tp2.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Classe modèle représentant une Note
 * Implémente Parcelable pour permettre le passage entre activités via Intent
 */
public class Note implements Parcelable {
    private String nom;
    private String description;
    private String date;
    private String priorite;
    private String photoPath; // Chemin de la photo capturée (optionnel)

    // Constructeur complet
    public Note(String nom, String description, String date, String priorite) {
        this.nom = nom;
        this.description = description;
        this.date = date;
        this.priorite = priorite;
        this.photoPath = null;
    }

    // Constructeur avec photo
    public Note(String nom, String description, String date, String priorite, String photoPath) {
        this.nom = nom;
        this.description = description;
        this.date = date;
        this.priorite = priorite;
        this.photoPath = photoPath;
    }

    // Getters et Setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPriorite() {
        return priorite;
    }

    public void setPriorite(String priorite) {
        this.priorite = priorite;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    // Implémentation Parcelable pour passer l'objet via Intent
    protected Note(Parcel in) {
        nom = in.readString();
        description = in.readString();
        date = in.readString();
        priorite = in.readString();
        photoPath = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nom);
        dest.writeString(description);
        dest.writeString(date);
        dest.writeString(priorite);
        dest.writeString(photoPath);
    }
}

