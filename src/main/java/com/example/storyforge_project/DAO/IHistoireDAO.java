package com.example.storyforge_project.DAO;

import com.example.storyforge_project.model.Histoire;

import java.sql.SQLException;
import java.util.List;

public interface IHistoireDAO {
    void save(Histoire histoire) throws SQLException;
    List<Histoire> findAll() throws SQLException;
    void update(Histoire histoire) throws SQLException;
    void delete(int id) throws SQLException;
}