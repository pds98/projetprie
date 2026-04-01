package Service;


import Dao.PriereDAO;
import Modele.priere;

import java.sql.SQLException;
import java.util.List;

public class PriereService
{
    private PriereDAO priereDAO = new PriereDAO();

    public boolean ajouterPriere(priere p) throws SQLException {
        if (p.getNom() == null || p.getNom().isEmpty()) {
            System.err.println("Le nom de la prière est obligatoire.");
            return false;
        }
        return priereDAO.ajouter(p);
    }

    public priere obtenirPriere(int id) {
        return priereDAO.trouverParId(id);
    }

    public List<priere> obtenirToutesLesPrieres() {
        return priereDAO.trouverToutes();
    }

    public boolean modifierPriere(priere p) {
        if (priereDAO.trouverParId(p.getId()) == null) {
            System.err.println("Prière no see (ID: " + p.getId() + ").");
            return false;
        }
        return priereDAO.modifier(p);
    }

    public boolean supprimerPriere(int id) {
        if (priereDAO.trouverParId(id) == null) {
            System.err.println("Prière no see (ID: " + id + ").");
            return false;
        }
        return priereDAO.supprimer(id);
    }
}
