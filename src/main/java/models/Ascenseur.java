package models;

import models.commandes.utilisateur.exterieur.AppelDescendre;
import models.commandes.utilisateur.exterieur.AppelMonter;
import models.commandes.utilisateur.interieur.DemandeArretUrgence;
import models.commandes.utilisateur.interieur.DeplacementNiveau;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

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
        System.out.println("Niveau ascenseur:  " + ascenseur.getEngine().getActualLevel());
        System.out.println("Hauteur ascenseur: " + ascenseur.getEngine().getActualHeight());

        //Boutons extérieur
        Bouton descendre = ascenseur.getExternalButtons().get(0);
        Bouton monter = ascenseur.getExternalButtons().get(1);

        //Boutons intérieur
        Bouton arretUrgence = ascenseur.getInternalButtons().get(0);
        Bouton deplacementNiveau = ascenseur.getInternalButtons().get(1);

        descendre.press("4");
        descendre.press("2");
        try {
            sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        deplacementNiveau.press("1");
    }
}
