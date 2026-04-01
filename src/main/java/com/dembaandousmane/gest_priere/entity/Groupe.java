package com.dembaandousmane.gest_priere.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "groupe")
public class Groupe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdGroupe")
    private int idGroupe;

    private String nom;
    private String description;

    public Groupe() {}

    public Groupe(String nom, String description) {
        this.nom = nom;
        this.description = description;
    }

    public int getIdGroupe() { return idGroupe; }
    public void setIdGroupe(int idGroupe) { this.idGroupe = idGroupe; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
