package models;

import java.util.ArrayList;

public class Ascenseur {

    /**
     * Moteur de l'ascenseur
     */
    private Moteur engine;

    /**
     * Boutons externes pour commander l'ascenseur
     */
    private ArrayList<Bouton> externalButtons;

    /**
     * Boutons internes pour commander l'ascenseur
     */
    private ArrayList<Bouton> internalButtons;

    /**
     * Constructeur par défaut
     * @param levels niveaux
     */
    public Ascenseur(int levels, int defaultLevel) {
        this.engine = new Moteur(levels, defaultLevel);
        this.externalButtons = new ArrayList<>();
        this.internalButtons = new ArrayList<>();
    }
}
