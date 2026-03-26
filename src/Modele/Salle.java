package Modele;

public class Salle

{

    private int idSalle;
    private int capacite;
    private String statut;



    public Salle(int idSalle, int capacite, String statut) {
        this.idSalle = idSalle;
        this.capacite = capacite;
        this.statut = statut;
    }



    public int getIdSalle() {
        return idSalle;
    }

    public void setIdSalle(int idSalle) {
        this.idSalle = idSalle;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }


    public boolean estDisponible() {
        return this.statut.equalsIgnoreCase("disponible");
    }

    public void libererSalle() {
        this.statut = "disponible";
        System.out.println("La salle " + idSalle + " est maintenant libérée.");
    }

    public void afficherInformation() {
        System.out.println("=== Salle ===");
        System.out.println("ID Salle  : " + idSalle);
        System.out.println("Capacité  : " + capacite);
        System.out.println("Statut    : " + statut);
    }

}
