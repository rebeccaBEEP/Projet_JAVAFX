package com.example.storyforge_project.DAO
;

import com.example.storyforge_project.DAO.PersonnageDAO;
import com.example.storyforge_project.database.ConnexionBD;
import com.example.storyforge_project.model.Histoire;
import com.example.storyforge_project.model.Personnage;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class HistoireDAO implements IHistoireDAO {

    private PersonnageDAO personnageDAO = new PersonnageDAO();

    public void save(Histoire histoire) throws SQLException {
        String sql = "INSERT INTO histoire (titre, auteur, resume) VALUES (?, ?, ?)";

        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement stmt = connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, histoire.getTitre());
            stmt.setString(2, histoire.getAuteur());
            stmt.setString(3, histoire.getResume());

            stmt.executeUpdate();

            ResultSet cles = stmt.getGeneratedKeys();
            if (cles.next()) {
                histoire.setId(cles.getInt(1));
            }
        }
    }

    public List<Histoire> findAll() throws SQLException {
        List<Histoire> histoires = new ArrayList<>();
        String sql = "SELECT * FROM histoire";

        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement stmt = connexion.prepareStatement(sql);
             ResultSet resultat = stmt.executeQuery()) {

            while (resultat.next()) {
                Histoire histoire = new Histoire(
                        resultat.getString("titre"),
                        resultat.getString("auteur"),
                        resultat.getString("resume")
                );
                histoire.setId(resultat.getInt("id"));

                // Charger les personnages associés à cette histoire
                List<Personnage> personnages = personnageDAO.findByHistoireId(histoire.getId());
                for (Personnage p : personnages) {
                    histoire.ajouterPersonnage(p);
                }

                histoires.add(histoire);
            }
        }
        return histoires;
    }

    public void update(Histoire histoire) throws SQLException {
        String sql = "UPDATE histoire SET titre = ?, auteur = ?, resume = ? WHERE id = ?";

        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement stmt = connexion.prepareStatement(sql)) {

            stmt.setString(1, histoire.getTitre());
            stmt.setString(2, histoire.getAuteur());
            stmt.setString(3, histoire.getResume());
            stmt.setInt(4, histoire.getId());

            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM histoire WHERE id = ?";

        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement stmt = connexion.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}