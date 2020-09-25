package models;

import controllers.ControleurMoteur;

public class Ascenseur {

    /**
     * Moteur de l'ascenseur
     */
    private Moteur engine;

    /**
     * Constructeur par défaut
     * @param levels niveaux
     */
    public Ascenseur(int levels, int defaultLevel) {
        this.engine = new Moteur(levels, defaultLevel);
    }

    public static void main(String[] args) {
        Ascenseur ascenseur = new Ascenseur(4,0);
        new ControleurMoteur(ascenseur.engine);
    }
}