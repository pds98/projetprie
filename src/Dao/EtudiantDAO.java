package Dao;

import Modele.Etudiant;
import Connexion.ConnexionDB ;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EtudiantDAO

{

    private Connection conn =  ConnexionDB.getInstance();

    // CREATE
    public boolean ajouter(Etudiant e) {
        String sql = "INSERT INTO etudiant (nom, prenom, telephone, email) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getNom());
            ps.setString(2, e.getPrenom());
            ps.setString(3, e.getTelephone());
            ps.setString(4, e.getEmail());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Erreur ajouter Etudiant : " + ex.getMessage());
            return false;
        }
    }
    //lire par id
    public Etudiant trouverParId(int id) {
        String sql = "SELECT * FROM etudiant WHERE idEtudiant = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Etudiant(
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("telephone"),
                        rs.getString("email"),
                        rs.getInt("idEtudiant")
                );
            }
        } catch (SQLException ex) {
            System.err.println("Eerreur : " + ex.getMessage());
        }
        return null;
    }

    // lis tois les etudiant
    public List<Etudiant> trouverTous() {
        List<Etudiant> liste = new ArrayList<>();
        String sql = "SELECT * FROM etudiant";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                liste.add(new Etudiant(
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("telephone"),
                        rs.getString("email"),
                        rs.getInt("idEtudiant")
                ));
            }
        } catch (SQLException ex) {
            System.err.println("Erreur trouverTous Etudiant : " + ex.getMessage());
        }
        return liste;
    }

    // pour modif
    public boolean modifier(Etudiant e) {
        String sql = "UPDATE etudiant SET nom=?, prenom=?, telephone=?, email=? WHERE idEtudiant=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getNom());
            ps.setString(2, e.getPrenom());
            ps.setString(3, e.getTelephone());
            ps.setString(4, e.getEmail());
            ps.setInt(5, e.getIdEtudiant());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Erreur modifier Etudiant : " + ex.getMessage());
            return false;
        }
    }

    // pour supprimer
    public boolean supprimer(int id) {
        String sql = "DELETE FROM etudiant WHERE idEtudiant=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Erreur supprimer Etudiant : " + ex.getMessage());
            return false;
        }
    }
}
