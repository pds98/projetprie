package com.dembaandousmane.gest_priere.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "salle")
public class Salle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSalle")
    private int idSalle;

    private int capacite;
    private String statut;

    public Salle() {}

    public Salle(int capacite, String statut) {
        this.capacite = capacite;
        this.statut = statut;
    }

    public int getIdSalle() { return idSalle; }
    public void setIdSalle(int idSalle) { this.idSalle = idSalle; }

    public int getCapacite() { return capacite; }
    public void setCapacite(int capacite) { this.capacite = capacite; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public void libererSalle() {
        this.statut = "disponible";
    }
}
