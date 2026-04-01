package Service;


import Dao.ReservationDAO;
import Dao.SalleDAO;
import Modele.Reservation;
import Modele.Salle;

import java.util.List;

public class ReservationService
{
    private ReservationDAO reservationDAO = new ReservationDAO();
    private SalleDAO salleDAO = new SalleDAO();

    public boolean creerReservation(Reservation r) {
        // Vérifier que la date de fin est après le début
        if (!r.getFin().isAfter(r.getDebut())) {
            System.err.println("La date de fin doit être après la date de début.");
            return false;
        }

        // Vérifier que le nombre de personnes est valide
        if (r.getNombrePersonnes() <= 0) {
            System.err.println("Le nombre de personnes doit être supérieur à 0.");
            return false;
        }

        // Vérifier que la salle existe et est disponible
        Salle salle = salleDAO.trouverParId(r.getIdSalle());
        if (salle == null) {
            System.err.println("Salle introuvable (ID: " + r.getIdSalle() + ").");
            return false;
        }
        if (!"disponible".equalsIgnoreCase(salle.getStatut())) {
            System.err.println("La salle n'est pas disponible.");
            return false;
        }

        // Vérifier que la capacité est suffisante
        if (r.getNombrePersonnes() > salle.getCapacite()) {
            System.err.println("Le nombre de personnes dépasse la capacité de la salle (" + salle.getCapacite() + ").");
            return false;
        }

        // Marquer la salle comme occupée
        salle.setStatut("occupée");
        salleDAO.modifier(salle);

        r.setEstReserver(true);
        return reservationDAO.ajouter(r);
    }

    public Reservation obtenirReservation(int id) {
        return reservationDAO.trouverParId(id);
    }

    public List<Reservation> obtenirToutesLesReservations() {
        return reservationDAO.trouverToutes();
    }

    public List<Reservation> obtenirReservationsParEtudiant(int idEtudiant) {
        return reservationDAO.trouverParEtudiant(idEtudiant);
    }

    public boolean modifierReservation(Reservation r) {
        if (reservationDAO.trouverParId(r.getId()) == null) {
            System.err.println("Réservation introuvable (ID: " + r.getId() + ").");
            return false;
        }
        if (!r.getFin().isAfter(r.getDebut())) {
            System.err.println("La date de fin doit être après la date de début.");
            return false;
        }
        return reservationDAO.modifier(r);
    }

    public boolean annulerReservation(int id) {
        Reservation r = reservationDAO.trouverParId(id);
        if (r == null) {
            System.err.println("Réservation introuvable (ID: " + id + ").");
            return false;
        }

        // Libérer la salle
        Salle salle = salleDAO.trouverParId(r.getIdSalle());
        if (salle != null) {
            salle.libererSalle();
            salleDAO.modifier(salle);
        }

        r.annulerReservation();
        return reservationDAO.modifier(r);
    }

    public boolean supprimerReservation(int id) {
        if (reservationDAO.trouverParId(id) == null) {
            System.err.println("Réservation introuvable (ID: " + id + ").");
            return false;
        }
        return reservationDAO.supprimer(id);
    }
}
