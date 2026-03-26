package Modele;

public class Etudiant
{

    private String nomEtu;
    private String prenomEtu;
    private String telephone;
    private String email;




    public Etudiant(String nomEtu, String prenomEtu, String telephone, String email, int idEtudiant) {
        this.nomEtu = nomEtu;
        this.prenomEtu = prenomEtu;
        this.telephone = telephone;
        this.email = email;
        this.idEtudiant = idEtudiant;
    }



    private int idEtudiant;

    public int getIdEtudiant() {
        return idEtudiant;
    }

    public void setIdEtudiant(int idEtudiant) {
        this.idEtudiant = idEtudiant;
    }

    public String getNomEtu() {
        return nomEtu;
    }

    public void setNomEtu(String nomEtu) {
        this.nomEtu = nomEtu;
    }

    public String getPrenomEtu() {
        return prenomEtu;
    }

    public void setPrenomEtu(String prenomEtu) {
        this.prenomEtu = prenomEtu;
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
        System.out.println(prenomEtu + " " + nomEtu + " a réserver une salle.");
    }

    public void annulerReservation() {
        System.out.println(prenomEtu + " " + nomEtu + " a annulr sa réservation.");
    }

    public void consulterReservation() {
        System.out.println(prenomEtu + " " + nomEtu + " consulter ses réservations.");
    }

}
