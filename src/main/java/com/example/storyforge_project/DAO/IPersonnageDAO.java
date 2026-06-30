package com.example.storyforge_project.DAO;

import com.example.storyforge_project.model.Personnage;

import java.sql.SQLException;
import java.util.List;

public interface IPersonnageDAO {
    void save(Personnage personnage, int histoireId) throws SQLException;
    List<Personnage> findByHistoireId(int histoireId) throws SQLException;
    void update(Personnage personnage) throws SQLException;
    void delete(int id) throws SQLException;
}