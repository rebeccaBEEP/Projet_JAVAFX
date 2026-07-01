package com.example.storyforge_project.model;

import java.util.Map;

public class StatistiquesScenes {

    private final int nombrePersonnages;
    private final int nombreScenes;
    private final Map<StatutScene, Long> scenesParStatut;

    public StatistiquesScenes(int nombrePersonnages, int nombreScenes, Map<StatutScene, Long> scenesParStatut) {
        this.nombrePersonnages = nombrePersonnages;
        this.nombreScenes = nombreScenes;
        this.scenesParStatut = scenesParStatut;
    }

    public int getNombrePersonnages() { return nombrePersonnages; }
    public int getNombreScenes() { return nombreScenes; }
    public Map<StatutScene, Long> getScenesParStatut() { return scenesParStatut; }

    public long getNombreScenes(StatutScene statut) {
        return scenesParStatut.getOrDefault(statut, 0L);
    }
}