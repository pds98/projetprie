package Service;
import Dao.EtudiantDAO;
import Modele.Etudiant;

import java.util.List;

public class EtudiantService
{
    private EtudiantDAO etudiantDAO = new EtudiantDAO();

    public boolean inscrireEtudiant(Etudiant e) {
        if (e.getNom() == null || e.getNom().isEmpty()) {
            System.err.println("Le nom de l'étudiant est obligatoire.");
            return false;
        }
        if (e.getEmail() == null || e.getEmail().isEmpty()) {
            System.err.println("L'email de l'étudiant est obligatoire.");
            return false;
        }
        return etudiantDAO.ajouter(e);
    }

    public Etudiant obtenirEtudiant(int id) {
        return etudiantDAO.trouverParId(id);
    }

    public List<Etudiant> obtenirTousLesEtudiants() {
        return etudiantDAO.trouverTous();
    }

    public boolean modifierEtudiant(Etudiant e) {
        if (etudiantDAO.trouverParId(e.getIdEtudiant()) == null) {
            System.err.println("Étudiant introuvable (ID: " + e.getIdEtudiant() + ").");
            return false;
        }
        return etudiantDAO.modifier(e);
    }

    public boolean supprimerEtudiant(int id) {
        if (etudiantDAO.trouverParId(id) == null) {
            System.err.println("Étudiant introuvable (ID: " + id + ").");
            return false;
        }
        return etudiantDAO.supprimer(id);
    }
}
