package Connexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionDB
{

    private static final String URL      = "jdbc:mysql://localhost:3306/gestion_priere";
    private static final String USER     = "root";
    private static final String PASSWORD = "";

    private static Connection instance = null;

    private void ConnexionBD() {}

    public static Connection getInstance() {
        if (instance == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                instance = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ good .");
            } catch (ClassNotFoundException e) {
                System.err.println(" MySQL introuvable : " + e.getMessage());
            } catch (SQLException e) {
                System.err.println(" bleme de connexion : " + e.getMessage());
            }
        }
        return instance;
    }

    public static void fermerConnexion() {
        if (instance != null) {
            try {
                instance.close();
                instance = null;
                System.out.println("Connexion fermée.");
            } catch (SQLException e) {
                System.err.println("bleme lors de la fermeture : " + e.getMessage());
            }
        }
    }


}
