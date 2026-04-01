package Dao;

import Connexion.ConnexionDB;
import Modele.Evenement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EvenementDAO
{

    private Connection conn = ConnexionDB.getInstance();

    // CREATE
    public boolean ajouter(Evenement e) {
        String sql = "INSERT INTO evenement (nom, dateCreation, description, lieu, statut) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getNom());
            ps.setTimestamp(2, Timestamp.valueOf(e.getDateCreation()));
            ps.setString(3, e.getDescription());
            ps.setString(4, e.getLieu());
            ps.setString(5, e.getStatut());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Erreur ajouter Evenement : " + ex.getMessage());
            return false;
        }
    }

    // READ - par ID
    public Evenement trouverParId(int id) {
        String sql = "SELECT * FROM evenement WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException ex) {
            System.err.println("Erreur trouverParId Evenement : " + ex.getMessage());
        }
        return null;
    }

    // READ - tous
    public List<Evenement> trouverTous() {
        List<Evenement> liste = new ArrayList<>();
        String sql = "SELECT * FROM evenement";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                liste.add(mapRow(rs));
            }
        } catch (SQLException ex) {
            System.err.println("Erreur trouverTous Evenement : " + ex.getMessage());
        }
        return liste;
    }

    // READ - par statut
    public List<Evenement> trouverParStatut(String statut) {
        List<Evenement> liste = new ArrayList<>();
        String sql = "SELECT * FROM evenement WHERE statut = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statut);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                liste.add(mapRow(rs));
            }
        } catch (SQLException ex) {
            System.err.println("Erreur trouverParStatut Evenement : " + ex.getMessage());
        }
        return liste;
    }

    // UPDATE
    public boolean modifier(Evenement e) {
        String sql = "UPDATE evenement SET nom=?, dateCreation=?, description=?, lieu=?, statut=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getNom());
            ps.setTimestamp(2, Timestamp.valueOf(e.getDateCreation()));
            ps.setString(3, e.getDescription());
            ps.setString(4, e.getLieu());
            ps.setString(5, e.getStatut());
            ps.setInt(6, e.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Erreur modifier Evenement : " + ex.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean supprimer(int id) {
        String sql = "DELETE FROM evenement WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Erreur supprimer Evenement : " + ex.getMessage());
            return false;
        }
    }

    // Helper
    private Evenement mapRow(ResultSet rs) throws SQLException {
        return new Evenement(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getTimestamp("dateCreation").toLocalDateTime(),
                rs.getString("description"),
                rs.getString("lieu"),
                rs.getString("statut")
        );
    }
}
