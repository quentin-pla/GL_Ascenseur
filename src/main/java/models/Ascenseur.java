package models;

import models.commandes.utilisateur.exterieur.AppelDescendre;
import models.commandes.utilisateur.exterieur.AppelMonter;
import models.commandes.utilisateur.interieur.DemandeArretUrgence;
import models.commandes.utilisateur.interieur.DeplacementNiveau;

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
        linkButtons();
    }

    /**
     * Relier les boutons aux commandes
     */
    private void linkButtons() {
        //Ajout des boutons externes
        externalButtons.add(new Bouton(AppelDescendre.getInstance()));
        externalButtons.add(new Bouton(AppelMonter.getInstance()));
        //Ajout des boutons internes
        internalButtons.add(new Bouton(DemandeArretUrgence.getInstance()));
        internalButtons.add(new Bouton(DeplacementNiveau.getInstance()));
    }

    /*** GETTERS ***/

    public Moteur getEngine() {
        return engine;
    }

    public ArrayList<Bouton> getExternalButtons() {
        return externalButtons;
    }

    public ArrayList<Bouton> getInternalButtons() {
        return internalButtons;
    }

    public static void main(String[] args) {
        Ascenseur ascenseur = new Ascenseur(4,0);
        ascenseur.getExternalButtons().get(0).click();
        ascenseur.getExternalButtons().get(1).click();
        ascenseur.getInternalButtons().get(0).click();
        ascenseur.getInternalButtons().get(1).click();
    }
}
