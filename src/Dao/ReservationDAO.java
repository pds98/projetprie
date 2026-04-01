package Dao;

import Modele.Reservation;
import Modele.Etudiant;
import Connexion.ConnexionDB ;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO
{
    private Connection conn = ConnexionDB.getInstance();

    // CREATE
    public boolean ajouter(Reservation r) {
        String sql = "INSERT INTO reservation (idSalle, idEtudiant, idPriere, debut, fin, nombrePersonnes, motif, estReserver) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, r.getIdSalle());
            ps.setInt(2, r.getIdEtudiant());
            ps.setInt(3, r.getIdPriere());
            ps.setTimestamp(4, Timestamp.valueOf(r.getDebut()));
            ps.setTimestamp(5, Timestamp.valueOf(r.getFin()));
            ps.setInt(6, r.getNombrePersonnes());
            ps.setString(7, r.getMotif());
            ps.setBoolean(8, r.isEstReserver());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Erreur pour  ajouter Reservation : " + ex.getMessage());
            return false;
        }
    }

    // lire par ID
    public Reservation trouverParId(int id) {
        String sql = "SELECT * FROM reservation WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException ex) {
            System.err.println("Erreur trouverParId Reservation : " + ex.getMessage());
        }
        return null;
    }

    // lire toutes
    public List<Reservation> trouverToutes() {
        List<Reservation> liste = new ArrayList<>();
        String sql = "SELECT * FROM reservation";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                liste.add(mapRow(rs));
            }
        } catch (SQLException ex) {
            System.err.println("Erreur trouverToutes Reservation : " + ex.getMessage());
        }
        return liste;
    }

    //lire par étudiant
    public List<Reservation> trouverParEtudiant(int idEtudiant) {
        List<Reservation> liste = new ArrayList<>();
        String sql = "SELECT * FROM reservation WHERE idEtudiant = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEtudiant);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                liste.add(mapRow(rs));
            }
        } catch (SQLException ex) {
            System.err.println("Erreur trouverParEtudiant Reservation : " + ex.getMessage());
        }
        return liste;
    }

    // mise a jours
    public boolean modifier(Reservation r) {
        String sql = "UPDATE reservation SET idSalle=?, idEtudiant=?, idPriere=?, debut=?, fin=?, " +
                "nombrePersonnes=?, motif=?, estReserver=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, r.getIdSalle());
            ps.setInt(2, r.getIdEtudiant());
            ps.setInt(3, r.getIdPriere());
            ps.setTimestamp(4, Timestamp.valueOf(r.getDebut()));
            ps.setTimestamp(5, Timestamp.valueOf(r.getFin()));
            ps.setInt(6, r.getNombrePersonnes());
            ps.setString(7, r.getMotif());
            ps.setBoolean(8, r.isEstReserver());
            ps.setInt(9, r.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("error on peut pas modifeir  " + ex.getMessage());
            return false;
        }
    }

    // supprimer
    public boolean supprimer(int id) {
        String sql = "DELETE FROM reservation WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Erreur supprimer Reservation : " + ex.getMessage());
            return false;
        }
    }

    // mapping ResultSet → Reservation
    private Reservation mapRow(ResultSet rs) throws SQLException {
        return new Reservation(
                rs.getInt("id"),
                rs.getInt("idSalle"),
                rs.getInt("idEtudiant"),
                rs.getInt("idPriere"),
                rs.getTimestamp("debut").toLocalDateTime(),
                rs.getTimestamp("fin").toLocalDateTime(),
                rs.getInt("nombrePersonnes"),
                rs.getString("motif"),
                rs.getBoolean("estReserver")
        );
    }
}
