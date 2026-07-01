package com.example.storyforge_project.service;

import com.example.storyforge_project.DAO.ISceneDAO;
import com.example.storyforge_project.DAO.SceneDAO;
import com.example.storyforge_project.model.Histoire;
import com.example.storyforge_project.model.Personnage;
import com.example.storyforge_project.model.Scene;
import com.example.storyforge_project.model.StatistiquesScenes;
import com.example.storyforge_project.model.StatutScene;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SceneService {

    private final ISceneDAO sceneDAO;

    public SceneService() {
        this.sceneDAO = new SceneDAO();
    }

    public SceneService(ISceneDAO sceneDAO) {
        this.sceneDAO = sceneDAO;
    }

    public List<Scene> getScenes(Histoire histoire) {
        try {
            List<Scene> scenes = sceneDAO.findByHistoireId(histoire.getId());
            for (Scene scene : scenes) {
                List<Integer> idsPersonnages = sceneDAO.findPersonnageIdsBySceneId(scene.getId());
                List<Personnage> personnages = histoire.getPersonnages().stream()
                        .filter(p -> idsPersonnages.contains(p.getId()))
                        .collect(Collectors.toList());
                scene.setPersonnages(personnages);
            }
            return scenes;
        } catch (SQLException e) {
            throw new RuntimeException("Impossible de charger les scènes : " + e.getMessage());
        }
    }

    public Scene creerScene(Histoire histoire, String titre, String lieu, String moment, String contenu,
                             int position, StatutScene statut, List<Personnage> personnages) {
        validerScene(histoire, titre, contenu, statut, position, null);
        validerPersonnagesAppartiennentAHistoire(histoire, personnages);

        Scene scene = new Scene(titre, lieu, moment, contenu, position, statut);
        scene.setHistoireId(histoire.getId());

        try {
            sceneDAO.save(scene);
            for (Personnage personnage : personnages) {
                sceneDAO.associerPersonnage(scene.getId(), personnage.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Impossible de créer la scène : " + e.getMessage());
        }

        scene.setPersonnages(personnages);
        return scene;
    }

    public void modifierScene(Histoire histoire, Scene scene, String titre, String lieu, String moment,
                               String contenu, int position, StatutScene statut, List<Personnage> personnages) {
        validerScene(histoire, titre, contenu, statut, position, scene.getId());
        validerPersonnagesAppartiennentAHistoire(histoire, personnages);

        scene.setTitre(titre);
        scene.setLieu(lieu);
        scene.setMoment(moment);
        scene.setContenu(contenu);
        scene.setPosition(position);
        scene.setStatut(statut);

        try {
            sceneDAO.update(scene);
            sceneDAO.viderPersonnages(scene.getId());
            for (Personnage personnage : personnages) {
                sceneDAO.associerPersonnage(scene.getId(), personnage.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Impossible de modifier la scène : " + e.getMessage());
        }

        scene.setPersonnages(personnages);
    }

    public void supprimerScene(Scene scene) {
        try {
            sceneDAO.delete(scene.getId());
        } catch (SQLException e) {
            throw new RuntimeException("Impossible de supprimer la scène : " + e.getMessage());
        }
    }

    public StatistiquesScenes getStatistiques(Histoire histoire) {
        List<Scene> scenes = getScenes(histoire);

        Map<StatutScene, Long> parStatut = scenes.stream()
                .collect(Collectors.groupingBy(Scene::getStatut, Collectors.counting()));

        return new StatistiquesScenes(histoire.getPersonnages().size(), scenes.size(), parStatut);
    }

    public List<Scene> filtrerParStatut(Histoire histoire, StatutScene statut) {
        if (statut == null) {
            return getScenes(histoire);
        }
        return getScenes(histoire).stream()
                .filter(s -> s.getStatut() == statut)
                .collect(Collectors.toList());
    }

    public List<Scene> filtrerParPersonnage(Histoire histoire, Personnage personnage) {
        if (personnage == null) {
            return getScenes(histoire);
        }
        return getScenes(histoire).stream()
                .filter(s -> s.getPersonnages().stream().anyMatch(p -> p.getId() == personnage.getId()))
                .collect(Collectors.toList());
    }

    public List<Scene> rechercherParMotCle(Histoire histoire, String motCle) {
        if (motCle == null || motCle.isBlank()) {
            return getScenes(histoire);
        }
        String recherche = motCle.toLowerCase();
        return getScenes(histoire).stream()
                .filter(s -> s.getTitre().toLowerCase().contains(recherche)
                        || s.getContenu().toLowerCase().contains(recherche))
                .collect(Collectors.toList());
    }

    public List<Scene> filtrerScenes(Histoire histoire, StatutScene statut, Personnage personnage, String motCle) {
        String recherche = (motCle == null) ? "" : motCle.toLowerCase();
        return getScenes(histoire).stream()
                .filter(s -> statut == null || s.getStatut() == statut)
                .filter(s -> personnage == null || s.getPersonnages().stream().anyMatch(p -> p.getId() == personnage.getId()))
                .filter(s -> recherche.isBlank() || s.getTitre().toLowerCase().contains(recherche) || s.getContenu().toLowerCase().contains(recherche))
                .collect(Collectors.toList());
    }

    private void validerScene(Histoire histoire, String titre, String contenu, StatutScene statut,
                               int position, Integer idSceneEnCoursDeModification) {
        if (titre == null || titre.isBlank()) {
            throw new IllegalArgumentException("Le titre de la scène ne peut pas être vide.");
        }
        if (contenu == null || contenu.isBlank()) {
            throw new IllegalArgumentException("Le contenu de la scène ne peut pas être vide.");
        }
        if (statut == null) {
            throw new IllegalArgumentException("La scène doit posséder un statut.");
        }

        try {
            boolean positionDejaUtilisee = sceneDAO.findByHistoireId(histoire.getId()).stream()
                    .anyMatch(s -> s.getPosition() == position
                            && (idSceneEnCoursDeModification == null || s.getId() != idSceneEnCoursDeModification));
            if (positionDejaUtilisee) {
                throw new IllegalArgumentException("Cette position est déjà utilisée par une autre scène de l'histoire.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Impossible de vérifier l'unicité de la position : " + e.getMessage());
        }
    }

    private void validerPersonnagesAppartiennentAHistoire(Histoire histoire, List<Personnage> personnages) {
        for (Personnage personnage : personnages) {
            boolean appartientAHistoire = histoire.getPersonnages().stream()
                    .anyMatch(p -> p.getId() == personnage.getId());
            if (!appartientAHistoire) {
                throw new IllegalArgumentException(
                        "Le personnage \"" + personnage.getNom() + "\" n'appartient pas à cette histoire.");
            }
        }
    }
}