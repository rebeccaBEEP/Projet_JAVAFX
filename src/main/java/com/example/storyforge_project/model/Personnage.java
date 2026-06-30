package com.example.storyforge_project.model;

public class Personnage {

    private String nom;
    private String role;
    private String description;private int id;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }


    public Personnage(String nom, String role, String description){
        this.nom = nom;
        this.role = role;
        this.description = description;
    }
    public String getNom(){
        return nom;
    }
    public String getRole(){
        return role;
    }
    public String getDescription(){
        return description;
    }


    public void setNom(String nom){
        this.nom = nom;
    }
    public void setRole(String role){
        this.role = role;
    }
    public void setDescription(String description){
        this.description = description;
    }


    @Override
    public String toString() {
        return nom + " (" + role + ")";
    }


}


