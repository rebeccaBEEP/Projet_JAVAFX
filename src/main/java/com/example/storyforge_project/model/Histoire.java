package com.example.storyforge_project.model;
import java.util.List;
import java.util.ArrayList;


public class Histoire {


        private String titre;
        private String auteur;
        private String resume;
        private List<Personnage> personnages;
        private int id;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public Histoire(String titre, String auteur, String resume) {
            this.titre = titre;
            this.auteur = auteur;
            this.resume = resume;
            this.personnages = new ArrayList<>();
        }

    public void ajouterPersonnage(Personnage personnage) {
        personnages.add(personnage);
    }

    public void supprimerPersonnage(Personnage personnage) {
        personnages.remove(personnage);
    }

        public String getTitre() { return titre; }
        public String getAuteur() { return auteur; }
        public String getResume() { return resume; }
        public List<Personnage> getPersonnages() { return personnages; }



        public void setTitre(String titre) { this.titre = titre; }
        public void setAuteur(String auteur) { this.auteur = auteur; }
        public void setResume(String resume) { this.resume = resume; }



    @Override
    public String toString() {
        return titre + " (" + auteur + ")";
    }
    }



