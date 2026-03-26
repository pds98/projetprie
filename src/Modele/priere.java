package Modele;

public class priere {
    private int id;

    public priere(int id, String nom, String description) {
        this.id = id;
        this.nom = nom;
        this.description = description;
    }

    private String nom;
    private String description;
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public void afficherInfos() {
        System.out.println("=== Prière ===");
        System.out.println("ID          : " + id);
        System.out.println("Nom         : " + nom);
        System.out.println("Description : " + description);
    }

}
