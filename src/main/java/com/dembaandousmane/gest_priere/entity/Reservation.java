package com.dembaandousmane.gest_priere.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int idSalle;
    private int idEtudiant;
    private int idPriere;

    private LocalDateTime debut;
    private LocalDateTime fin;
    private int nombrePersonnes;
    private String motif;
    private boolean estReserver;

    public Reservation() {}

    public Reservation(int idSalle, int idEtudiant, int idPriere,
                       LocalDateTime debut, LocalDateTime fin,
                       int nombrePersonnes, String motif, boolean estReserver) {
        this.idSalle = idSalle;
        this.idEtudiant = idEtudiant;
        this.idPriere = idPriere;
        this.debut = debut;
        this.fin = fin;
        this.nombrePersonnes = nombrePersonnes;
        this.motif = motif;
        this.estReserver = estReserver;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdSalle() { return idSalle; }
    public void setIdSalle(int idSalle) { this.idSalle = idSalle; }

    public int getIdEtudiant() { return idEtudiant; }
    public void setIdEtudiant(int idEtudiant) { this.idEtudiant = idEtudiant; }

    public int getIdPriere() { return idPriere; }
    public void setIdPriere(int idPriere) { this.idPriere = idPriere; }

    public LocalDateTime getDebut() { return debut; }
    public void setDebut(LocalDateTime debut) { this.debut = debut; }

    public LocalDateTime getFin() { return fin; }
    public void setFin(LocalDateTime fin) { this.fin = fin; }

    public int getNombrePersonnes() { return nombrePersonnes; }
    public void setNombrePersonnes(int nombrePersonnes) { this.nombrePersonnes = nombrePersonnes; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }

    public boolean isEstReserver() { return estReserver; }
    public void setEstReserver(boolean estReserver) { this.estReserver = estReserver; }
}
