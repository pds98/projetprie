package com.dembaandousmane.gest_priere.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evenement")
public class Evenement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nom;
    private LocalDateTime dateCreation;
    private String description;
    private String lieu;
    private String statut;

    public Evenement() {}

    public Evenement(String nom, LocalDateTime dateCreation, String description, String lieu, String statut) {
        this.nom = nom;
        this.dateCreation = dateCreation;
        this.description = description;
        this.lieu = lieu;
        this.statut = statut;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}
