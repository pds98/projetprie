package Modele;

import java.time.LocalDateTime;

public class Reservation {
    private int id;

    public Reservation(int id, int idSalle, int idEtudiant, int idPriere, LocalDateTime debut, LocalDateTime fin, int nombrePersonnes, String motif, boolean estReserver) {
        this.id = id;
        this.idSalle = idSalle;
        this.idEtudiant = idEtudiant;
        this.idPriere = idPriere;
        this.debut = debut;
        this.fin = fin;
        this.nombrePersonnes = nombrePersonnes;
        this.motif = motif;
        this.estReserver = estReserver;
    }

    private int idSalle;
    private int idEtudiant;
    private int idPriere;
    private LocalDateTime debut;
    private LocalDateTime fin;
    private int nombrePersonnes;
    private String motif;
    private boolean estReserver;


    public int getIdEtudiant() {
        return idEtudiant;
    }

    public void setIdEtudiant(int idEtudiant) {
        this.idEtudiant = idEtudiant;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdSalle() {
        return idSalle;
    }

    public void setIdSalle(int idSalle) {
        this.idSalle = idSalle;
    }

    public int getIdPriere() {
        return idPriere;
    }

    public void setIdPriere(int idPriere) {
        this.idPriere = idPriere;
    }

    public LocalDateTime getDebut() {
        return debut;
    }

    public void setDebut(LocalDateTime debut) {
        this.debut = debut;
    }

    public LocalDateTime getFin() {
        return fin;
    }

    public void setFin(LocalDateTime fin) {
        this.fin = fin;
    }

    public int getNombrePersonnes() {
        return nombrePersonnes;
    }

    public void setNombrePersonnes(int nombrePersonnes) {
        this.nombrePersonnes = nombrePersonnes;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public boolean isEstReserver() {
        return estReserver;
    }

    public void setEstReserver(boolean estReserver) {
        this.estReserver = estReserver;
    }


    public void creerReservation() {
        this.estReserver = true;
        System.out.println("Réservation " + id + " créée.");
    }

    public void annulerReservation() {
        this.estReserver = false;
        System.out.println("Réservation " + id + " annulée.");
    }

    public void modifierReservation(LocalDateTime nouveauDebut, LocalDateTime nouveauFin, String nouveauMotif) {
        this.debut = nouveauDebut;
        this.fin = nouveauFin;
        this.motif = nouveauMotif;
        System.out.println("Réservation " + id + " modifiée.");
    }

    public void confirmerReservation() {
        this.estReserver = true;
        System.out.println("Réservation " + id + " confirmée.");
    }

    public void afficherReservations() {
        System.out.println("=== Réservation ===");
        System.out.println("ID            : " + id);
        System.out.println("ID Salle      : " + idSalle);
        System.out.println("ID Etudiant   : " + idEtu);
        System.out.println("ID Priere     : " + idPriere);
        System.out.println("Début         : " + debut);
        System.out.println("Fin           : " + fin);
        System.out.println("Nb personnes  : " + nombrePersonnes);
        System.out.println("Motif         : " + motif);
        System.out.println("Est reserve   : " + estReserver);


}
