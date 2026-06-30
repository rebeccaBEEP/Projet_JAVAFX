package com.example.storyforge_project.DAO;

import com.example.storyforge_project.database.ConnexionBD;
import com.example.storyforge_project.model.Personnage;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class PersonnageDAO implements IPersonnageDAO {

    public void save(Personnage personnage, int histoireId) throws SQLException {
        String sql = "INSERT INTO personnage (nom, role, description, histoire_id) VALUES (?, ?, ?, ?)";

        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement stmt = connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, personnage.getNom());
            stmt.setString(2, personnage.getRole());
            stmt.setString(3, personnage.getDescription());
            stmt.setInt(4, histoireId);

            stmt.executeUpdate();

            ResultSet cles = stmt.getGeneratedKeys();
            if (cles.next()) {
                personnage.setId(cles.getInt(1));
            }
        }
    }




    public List<Personnage> findByHistoireId(int histoireId) throws SQLException {
        List<Personnage> personnages = new ArrayList<>();
        String sql = "SELECT * FROM personnage WHERE histoire_id = ?";

        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement stmt = connexion.prepareStatement(sql)) {

            stmt.setInt(1, histoireId);
            ResultSet resultat = stmt.executeQuery();

            while (resultat.next()) {
                Personnage personnage = new Personnage(
                        resultat.getString("nom"),
                        resultat.getString("role"),
                        resultat.getString("description")
                );
                personnage.setId(resultat.getInt("id"));
                personnages.add(personnage);
            }
        }
        return personnages;
    }


    public void update(Personnage personnage) throws SQLException {
        String sql = "UPDATE personnage SET nom = ?, role = ?, description = ? WHERE id = ?";

        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement stmt = connexion.prepareStatement(sql)) {

            stmt.setString(1, personnage.getNom());
            stmt.setString(2, personnage.getRole());
            stmt.setString(3, personnage.getDescription());
            stmt.setInt(4, personnage.getId());

            stmt.executeUpdate();
        }
    }



    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM personnage WHERE id = ?";

        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement stmt = connexion.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }




}