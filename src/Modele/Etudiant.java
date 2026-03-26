package Modele;

public class Etudiant
{
    private int idEtudiant;
    private String nom;
    private String prenom;
    private String telephone;
    private String email;




    public Etudiant(String nom, String prenom, String telephone, String email, int idEtudiant) {
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.email = email;
        this.idEtudiant = idEtudiant;
    }




    public int getIdEtudiant() {
        return idEtudiant;
    }

    public void setIdEtudiant(int idEtudiant) {
        this.idEtudiant = idEtudiant;
    }

    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom= prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    //ethodes

    public void reserverSalle() {
        System.out.println(prenom + " " + nom + " a reserver une salle.");
    }

    public void annulerReservation() {
        System.out.println(prenom + " " + nom + " a annulr sa reservation.");
    }

    public void consulterReservation() {
        System.out.println(prenom + " " + nom + " consulter ses reservations.");
    }

}
