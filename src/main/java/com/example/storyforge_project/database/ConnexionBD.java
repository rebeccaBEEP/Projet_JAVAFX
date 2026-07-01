package com.example.storyforge_project.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionBD {

    private static final String URL = "jdbc:mysql://localhost:3306/storyforge";
    private static final String UTILISATEUR = "root";
    private static final String MOT_DE_PASSE = "Fatouka1234&";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE);
    }

    public static void main(String[] args) {
        try {
            Connection connexion = getConnection();
            System.out.println("Connexion réussie !");
            connexion.close();
        } catch (SQLException e) {
            System.out.println("Erreur de connexion : " + e.getMessage());
        }
    }
}