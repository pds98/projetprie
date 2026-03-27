package Modele;

public class Groupe {


    private int IdGroupe;
    private String nom;
    private String description;


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getIdGroupe() {
        return IdGroupe;
    }

    public void setIdGroupe(int idGroupe) {
        IdGroupe = idGroupe;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Groupe(String nom, String description){
        this.nom = nom;
        this.description = description;
    }

    public void AvoirInfo(){

    }
}
