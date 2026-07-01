package com.example.storyforge_project.service;

import com.example.storyforge_project.DAO.HistoireDAO;
import com.example.storyforge_project.DAO.IHistoireDAO;
import com.example.storyforge_project.DAO.IPersonnageDAO;
import com.example.storyforge_project.DAO.PersonnageDAO;
import com.example.storyforge_project.model.Histoire;
import com.example.storyforge_project.model.Personnage;

import java.sql.SQLException;
import java.util.List;

public class HistoireService {

    private IHistoireDAO histoireDAO;
    private IPersonnageDAO personnageDAO;

    public HistoireService() {
        this.histoireDAO = new HistoireDAO();
        this.personnageDAO = new PersonnageDAO();
    }

    public HistoireService(IHistoireDAO histoireDAO, IPersonnageDAO personnageDAO) {
        this.histoireDAO = histoireDAO;
        this.personnageDAO = personnageDAO;
    }


    public List<Histoire> getHistoires() {
        try {
            return histoireDAO.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Impossible de charger les histoires : " + e.getMessage());
        }
    }

    public Histoire creerHistoire(String titre, String auteur, String resume) {
        validerHistoire(titre, auteur);
        Histoire histoire = new Histoire(titre, auteur, resume);
        try {
            histoireDAO.save(histoire);
        } catch (SQLException e) {
            throw new RuntimeException("Impossible de créer l'histoire : " + e.getMessage());
        }
        return histoire;
    }

    public void supprimerHistoire(Histoire histoire) {
        try {
            histoireDAO.delete(histoire.getId());
        } catch (SQLException e) {
            throw new RuntimeException("Impossible de supprimer l'histoire : " + e.getMessage());
        }
    }

    public void modifierHistoire(Histoire histoire, String titre, String auteur, String resume) {
        validerHistoire(titre, auteur);
        histoire.setTitre(titre);
        histoire.setAuteur(auteur);
        histoire.setResume(resume);
        try {
            histoireDAO.update(histoire);
        } catch (SQLException e) {
            throw new RuntimeException("Impossible de modifier l'histoire : " + e.getMessage());
        }
    }

    private void validerHistoire(String titre, String auteur) {
        if (titre == null || titre.isBlank()) {
            throw new IllegalArgumentException("Le titre ne peut pas être vide.");
        }
        if (auteur == null || auteur.isBlank()) {
            throw new IllegalArgumentException("L'auteur ne peut pas être vide.");
        }
    }

    public Personnage ajouterPersonnage(Histoire histoire, String nom, String role, String description) {
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide.");
        }
        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("Le rôle ne peut pas être vide.");
        }

        boolean existeDeja = histoire.getPersonnages().stream()
                .anyMatch(p -> p.getNom().equals(nom));
        if (existeDeja) {
            throw new IllegalArgumentException("Un personnage avec ce nom existe déjà dans cette histoire.");
        }

        Personnage personnage = new Personnage(nom, role, description);
        try {
            personnageDAO.save(personnage, histoire.getId());
        } catch (SQLException e) {
            throw new RuntimeException("Impossible d'ajouter le personnage : " + e.getMessage());
        }
        histoire.ajouterPersonnage(personnage);
        return personnage;
    }

    public void modifierPersonnage(Histoire histoire, Personnage personnage, String nom, String role, String description) {
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide.");
        }
        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("Le rôle ne peut pas être vide.");
        }

        boolean existeDeja = histoire.getPersonnages().stream()
                .anyMatch(p -> p.getNom().equals(nom) && p != personnage);
        if (existeDeja) {
            throw new IllegalArgumentException("Un personnage avec ce nom existe déjà dans cette histoire.");
        }

        personnage.setNom(nom);
        personnage.setRole(role);
        personnage.setDescription(description);
        try {
            personnageDAO.update(personnage);
        } catch (SQLException e) {
            throw new RuntimeException("Impossible de modifier le personnage : " + e.getMessage());
        }
    }

    public void supprimerPersonnage(Histoire histoire, Personnage personnage) {
        try {
            personnageDAO.delete(personnage.getId());
        } catch (SQLException e) {
            throw new RuntimeException("Impossible de supprimer le personnage : " + e.getMessage());
        }
        histoire.supprimerPersonnage(personnage);
    }
}