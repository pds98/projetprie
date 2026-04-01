package Dao;

import Connexion.ConnexionDB;
import Modele.Salle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalleDAO
{

    private Connection conn = ConnexionDB.getInstance();

    // CREATE
    public boolean ajouter(Salle s) {
        String sql = "INSERT INTO salle (capacite, statut) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, s.getCapacite());
            ps.setString(2, s.getStatut());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Erreur ajouter Salle : " + ex.getMessage());
            return false;
        }
    }

    // lire  par ID
    public Salle trouverParId(int id) {
        String sql = "SELECT * FROM salle WHERE idSalle = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Salle(
                        rs.getInt("idSalle"),
                        rs.getInt("capacite"),
                        rs.getString("statut")
                );
            }
        } catch (SQLException ex) {
            System.err.println("Erreur trouverParId Salle : " + ex.getMessage());
        }
        return null;
    }

    // lire toutes
    public List<Salle> trouverToutes() {
        List<Salle> liste = new ArrayList<>();
        String sql = "SELECT * FROM salle";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                liste.add(new Salle(
                        rs.getInt("idSalle"),
                        rs.getInt("capacite"),
                        rs.getString("statut")
                ));
            }
        } catch (SQLException ex) {
            System.err.println("Erreur trouverToutes Salle : " + ex.getMessage());
        }
        return liste;
    }

    // voir salles disponibles
    public List<Salle> trouverDisponibles() {
        List<Salle> liste = new ArrayList<>();
        String sql = "SELECT * FROM salle WHERE statut = 'disponible'";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                liste.add(new Salle(
                        rs.getInt("idSalle"),
                        rs.getInt("capacite"),
                        rs.getString("statut")
                ));
            }
        } catch (SQLException ex) {
            System.err.println("on peu t pas trouverDisponibles Salle : " + ex.getMessage());
        }
        return liste;
    }

    // MJ
    public boolean modifier(Salle s) {
        String sql = "UPDATE salle SET capacite=?, statut=? WHERE idSalle=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, s.getCapacite());
            ps.setString(2, s.getStatut());
            ps.setInt(3, s.getIdSalle());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Erreur modifier Salle : " + ex.getMessage());
            return false;
        }
    }

    // supprimer
    public boolean supprimer(int id) {
        String sql = "DELETE FROM salle WHERE idSalle=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Erreur supprimer Salle : " + ex.getMessage());
            return false;
        }
    }
}
