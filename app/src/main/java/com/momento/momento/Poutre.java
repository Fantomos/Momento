package com.momento.momento;

public class Poutre {
    private int id;
    private String nom;
    private int type;
    private double longueur;
    private double force;

    public Poutre(int id,String nom, int type,double longueur,double force){
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.longueur = longueur;
        this.force = force;
    }

    public Poutre(String nom, int type,double longueur,double force){
        this.nom = nom;
        this.type = type;
        this.longueur = longueur;
        this.force = force;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public double getForce() {
        return force;
    }

    public void setForce(double force) {
        this.force = force;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getLongueur() {
        return longueur;
    }

    public void setLongueur(double longueur) {
        this.longueur = longueur;
    }

}