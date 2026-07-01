package com.example.storyforge_project;

import com.example.storyforge_project.DAO.HistoireDAO;
import com.example.storyforge_project.DAO.PersonnageDAO;
import com.example.storyforge_project.model.Histoire;
import com.example.storyforge_project.model.Personnage;
import com.example.storyforge_project.service.HistoireService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HistoireIntegrationTest {

    private HistoireService service;

    @BeforeEach
    public void setUp() {
        service = new HistoireService(new HistoireDAO(), new PersonnageDAO());
    }

    @Test
    public void sauvegarderEtRetrouverHistoire() {
        // Créer et sauvegarder une histoire
        Histoire histoire = service.creerHistoire("Test Integration", "Auteur Test", "Résumé test");
        assertTrue(histoire.getId() > 0);

        // Retrouver toutes les histoires et vérifier qu'elle est là
        List<Histoire> histoires = service.getHistoires();
        boolean trouve = histoires.stream()
                .anyMatch(h -> h.getTitre().equals("Test Integration"));
        assertTrue(trouve);

        // Nettoyage
        service.supprimerHistoire(histoire);
    }

    @Test
    public void sauvegarderEtRetrouverPersonnage() {
        // Créer une histoire
        Histoire histoire = service.creerHistoire("Test Personnage", "Auteur", "");

        // Ajouter un personnage
        Personnage personnage = service.ajouterPersonnage(histoire, "Emma Test", "Etudiante", "");
        assertTrue(personnage.getId() > 0);

        // Vérifier qu'il est dans la liste
        assertEquals(1, histoire.getPersonnages().size());
        assertEquals("Emma Test", histoire.getPersonnages().get(0).getNom());

        // Nettoyage
        service.supprimerHistoire(histoire);
    }

    @Test
    public void suppressionBienPriseEnCompte() {
        // Créer une histoire
        Histoire histoire = service.creerHistoire("A Supprimer", "Auteur", "");
        int id = histoire.getId();

        // Supprimer
        service.supprimerHistoire(histoire);

        // Vérifier qu'elle n'existe plus
        List<Histoire> histoires = service.getHistoires();
        boolean encorePresente = histoires.stream()
                .anyMatch(h -> h.getId() == id);
        assertFalse(encorePresente);
    }
}