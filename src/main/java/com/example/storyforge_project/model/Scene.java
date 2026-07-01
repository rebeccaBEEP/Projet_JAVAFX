package com.example.storyforge_project.model;

import java.util.ArrayList;
import java.util.List;

public class Scene {

    private int id;
    private String titre;
    private String lieu;
    private String moment;
    private String contenu;
    private int position;
    private StatutScene statut;
    private int histoireId;
    private List<Personnage> personnages;

    public Scene(String titre, String lieu, String moment, String contenu, int position, StatutScene statut) {
        this.titre = titre;
        this.lieu = lieu;
        this.moment = moment;
        this.contenu = contenu;
        this.position = position;
        this.statut = statut;
        this.personnages = new ArrayList<>();
    }

    public void ajouterPersonnage(Personnage personnage) {
        personnages.add(personnage);
    }

    public void retirerPersonnage(Personnage personnage) {
        personnages.remove(personnage);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }

    public String getMoment() { return moment; }
    public void setMoment(String moment) { this.moment = moment; }

    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }

    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }

    public StatutScene getStatut() { return statut; }
    public void setStatut(StatutScene statut) { this.statut = statut; }

    public int getHistoireId() { return histoireId; }
    public void setHistoireId(int histoireId) { this.histoireId = histoireId; }

    public List<Personnage> getPersonnages() { return personnages; }
    public void setPersonnages(List<Personnage> personnages) { this.personnages = personnages; }

    @Override
    public String toString() {
        return position + ". " + titre + " [" + statut + "]";
    }
}