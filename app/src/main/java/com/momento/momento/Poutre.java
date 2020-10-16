package com.momento.momento;

public class Poutre {
    private int id;
    private String nom;
    private int type;
    private double longueur;
    private double force;
    private double young;
    private double inertie;

    public Poutre(int id,String nom, int type,double longueur,double force,double young,double inertie){
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.longueur = longueur;
        this.force = force;
        this.young = young;
        this.inertie = inertie;
    }

    public Poutre(String nom, int type,double longueur,double force,double young,double inertie){
        this.nom = nom;
        this.type = type;
        this.longueur = longueur;
        this.force = force;
        this.young = young;
        this.inertie = inertie;
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

    public double getYoung() {
        return young;
    }

    public void setYoung(double young) {
        this.young = young;
    }

    public double getInertie() {
        return inertie;
    }

    public void setInertie(double inertie) {
        this.inertie = inertie;
    }
}