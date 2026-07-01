package com.example.storyforge_project.DAO;

import com.example.storyforge_project.database.ConnexionBD;
import com.example.storyforge_project.model.Scene;
import com.example.storyforge_project.model.StatutScene;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SceneDAO implements ISceneDAO {

    public void save(Scene scene) throws SQLException {
        String sql = "INSERT INTO scene (titre, lieu, moment, contenu, position, statut, histoire_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement stmt = connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, scene.getTitre());
            stmt.setString(2, scene.getLieu());
            stmt.setString(3, scene.getMoment());
            stmt.setString(4, scene.getContenu());
            stmt.setInt(5, scene.getPosition());
            stmt.setString(6, scene.getStatut().name());
            stmt.setInt(7, scene.getHistoireId());

            stmt.executeUpdate();

            ResultSet cles = stmt.getGeneratedKeys();
            if (cles.next()) {
                scene.setId(cles.getInt(1));
            }
        }
    }

    public List<Scene> findByHistoireId(int histoireId) throws SQLException {
        List<Scene> scenes = new ArrayList<>();
        String sql = "SELECT * FROM scene WHERE histoire_id = ? ORDER BY position";

        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement stmt = connexion.prepareStatement(sql)) {

            stmt.setInt(1, histoireId);
            ResultSet resultat = stmt.executeQuery();

            while (resultat.next()) {
                Scene scene = new Scene(
                        resultat.getString("titre"),
                        resultat.getString("lieu"),
                        resultat.getString("moment"),
                        resultat.getString("contenu"),
                        resultat.getInt("position"),
                        StatutScene.valueOf(resultat.getString("statut"))
                );
                scene.setId(resultat.getInt("id"));
                scene.setHistoireId(resultat.getInt("histoire_id"));
                scenes.add(scene);
            }
        }
        return scenes;
    }

    public void update(Scene scene) throws SQLException {
        String sql = "UPDATE scene SET titre = ?, lieu = ?, moment = ?, contenu = ?, position = ?, statut = ? " +
                "WHERE id = ?";

        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement stmt = connexion.prepareStatement(sql)) {

            stmt.setString(1, scene.getTitre());
            stmt.setString(2, scene.getLieu());
            stmt.setString(3, scene.getMoment());
            stmt.setString(4, scene.getContenu());
            stmt.setInt(5, scene.getPosition());
            stmt.setString(6, scene.getStatut().name());
            stmt.setInt(7, scene.getId());

            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM scene WHERE id = ?";

        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement stmt = connexion.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
        // Les lignes de scene_personnage liées sont supprimées automatiquement
        // grâce à ON DELETE CASCADE défini dans le script SQL.
    }

    public void associerPersonnage(int sceneId, int personnageId) throws SQLException {
        String sql = "INSERT INTO scene_personnage (scene_id, personnage_id) VALUES (?, ?)";

        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement stmt = connexion.prepareStatement(sql)) {

            stmt.setInt(1, sceneId);
            stmt.setInt(2, personnageId);
            stmt.executeUpdate();
        }
    }

    public void retirerPersonnage(int sceneId, int personnageId) throws SQLException {
        String sql = "DELETE FROM scene_personnage WHERE scene_id = ? AND personnage_id = ?";

        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement stmt = connexion.prepareStatement(sql)) {

            stmt.setInt(1, sceneId);
            stmt.setInt(2, personnageId);
            stmt.executeUpdate();
        }
    }

    public void viderPersonnages(int sceneId) throws SQLException {
        String sql = "DELETE FROM scene_personnage WHERE scene_id = ?";

        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement stmt = connexion.prepareStatement(sql)) {

            stmt.setInt(1, sceneId);
            stmt.executeUpdate();
        }
    }

    public List<Integer> findPersonnageIdsBySceneId(int sceneId) throws SQLException {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT personnage_id FROM scene_personnage WHERE scene_id = ?";

        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement stmt = connexion.prepareStatement(sql)) {

            stmt.setInt(1, sceneId);
            ResultSet resultat = stmt.executeQuery();

            while (resultat.next()) {
                ids.add(resultat.getInt("personnage_id"));
            }
        }
        return ids;
    }
}