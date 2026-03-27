package Modele;

import java.time.LocalDateTime;

public class Evenement {

    private int id;
    private String nom;
    private LocalDateTime dateCreation;

    public Evenement(int id, String nom, LocalDateTime dateCreation, String description, String lieu, String statut) {
        this.id = id;
        this.nom = nom;
        this.dateCreation = dateCreation;
        this.description = description;
        this.lieu = lieu;
        this.statut = statut;
    }

    private String description;
    private String lieu;
    private String statut;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public void AvoirInfo(){}
}
