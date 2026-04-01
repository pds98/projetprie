package Dao;


import Connexion.ConnexionDB;
import Modele.priere;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PriereDAO
{
    private Connection conn = ConnexionDB.getInstance();

    // creer connect
    public boolean ajouter(priere p) throws SQLException {
        String sql = "INSERT INTO priere (nom, description) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNom());
            ps.setString(2, p.getDescription());
            return ps.executeUpdate() > 0;
        }
    }

    // lire par ID
    public priere trouverParId(int id) {
        String sql = "SELECT * FROM priere WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new priere(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("description")
                );
            }
        } catch (SQLException ex) {
            System.err.println("Erreur trouverParId Priere : " + ex.getMessage());
        }
        return null;
    }

    // litre all
    public List<priere> trouverToutes() {
        List<priere> liste = new ArrayList<>();
        String sql = "SELECT * FROM priere";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                liste.add(new priere(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException ex) {
            System.err.println("Erreur trouverToutes Priere : " + ex.getMessage());
        }
        return liste;
    }

    // MAJ
    public boolean modifier(priere p) {
        String sql = "UPDATE priere SET nom=?, description=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNom());
            ps.setString(2, p.getDescription());
            ps.setInt(3, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Erreur modifier Priere : " + ex.getMessage());
            return false;
        }
    }

    // supprimer
    public boolean supprimer(int id) {
        String sql = "DELETE FROM priere WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Erreur supprimer Priere : " + ex.getMessage());
            return false;
        }
    }
}
