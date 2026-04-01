package Service;

import Dao.SalleDAO;
import Modele.Salle;

import java.util.List;

public class SalleService
{

    private SalleDAO salleDAO = new SalleDAO();

    public boolean ajouterSalle(Salle s) {
        if (s.getCapacite() <= 0) {
            System.err.println("La capacité doit être supérieure à 0.");
            return false;
        }
        return salleDAO.ajouter(s);
    }

    public Salle obtenirSalle(int id) {
        return salleDAO.trouverParId(id);
    }

    public List<Salle> obtenirToutesLesSalles() {
        return salleDAO.trouverToutes();
    }

    public List<Salle> obtenirSallesDisponibles() {
        return salleDAO.trouverDisponibles();
    }

    public boolean modifierSalle(Salle s) {
        if (salleDAO.trouverParId(s.getIdSalle()) == null) {
            System.err.println("Salle introuvable (ID: " + s.getIdSalle() + ").");
            return false;
        }
        return salleDAO.modifier(s);
    }

    public boolean libererSalle(int id) {
        Salle s = salleDAO.trouverParId(id);
        if (s == null) {
            System.err.println("Salle introuvable (ID: " + id + ").");
            return false;
        }
        s.libererSalle();
        return salleDAO.modifier(s);
    }

    public boolean supprimerSalle(int id) {
        if (salleDAO.trouverParId(id) == null) {
            System.err.println("Salle introuvable (ID: " + id + ").");
            return false;
        }
        return salleDAO.supprimer(id);
    }
}
