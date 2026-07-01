package com.example.storyforge_project.model;

public enum StatutScene {
    BROUILLON("Brouillon"),
    EN_COURS("En cours"),
    PRET_A_PUBLIER("Prêt à publier"),
    PUBLIEE("Publiée");

    private final String libelle;

    StatutScene(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    @Override
    public String toString() {
        return libelle;
    }
}