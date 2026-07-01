package com.example.storyforge_project.DAO;

import com.example.storyforge_project.model.Scene;

import java.sql.SQLException;
import java.util.List;

public interface ISceneDAO {

    void save(Scene scene) throws SQLException;

    List<Scene> findByHistoireId(int histoireId) throws SQLException;

    void update(Scene scene) throws SQLException;

    void delete(int id) throws SQLException;

    // Gestion de la relation N-N Scene
    void associerPersonnage(int sceneId, int personnageId) throws SQLException;

    void retirerPersonnage(int sceneId, int personnageId) throws SQLException;

    void viderPersonnages(int sceneId) throws SQLException;

    List<Integer> findPersonnageIdsBySceneId(int sceneId) throws SQLException;
}