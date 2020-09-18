package models;

import models.commandes.moteur.ArretProchainNiveau;
import models.commandes.moteur.ArretUrgence;
import models.commandes.moteur.Descendre;
import models.commandes.moteur.Monter;

import java.util.ArrayList;

public class Moteur {

    /**
     * Nombre de niveaux (constant)
     */
    private final int levels;

    /**
     * Niveau actuel
     */
    private int actualLevel;

    /**
     * Hauteur en mètres à laquelle il est situé
     */
    private double actualHeight;

    /**
     * Liste des prochains niveaux à se déplacer
     */
    private ArrayList<Integer> nextLevels;

    /**
     * Arrêt d'urgence
     */
    private boolean isEmergencyStopped;

    /**
     * Constructeur par défaut
     */
    public Moteur(int levels, int defaultLevel) {
        this.levels = levels;
        this.actualLevel = defaultLevel;
        this.actualHeight = getHeightFromLevel(defaultLevel);
        this.nextLevels = new ArrayList<>();
        this.isEmergencyStopped = false;
        linkCommands();
    }

    /**
     * Relie les commandes système au moteur
     */
    private void linkCommands() {
        ArretProchainNiveau.getInstance().linkEngine(this);
        ArretUrgence.getInstance().linkEngine(this);
        Descendre.getInstance().linkEngine(this);
        Monter.getInstance().linkEngine(this);
    }

    /**
     * Retourne la hauteur en mètres à partir d'un niveau
     * @param level niveau
     * @return hauteur en mètres
     */
    private double getHeightFromLevel(int level) {
        //La cabine de l'ascenseur fait 2 mètres de haut,
        //entre chaque niveau il y a une marge de 40cm
        return 2.4*level;
    }

    /*** GETTERS & SETTERS ***/

    public int getActualLevel() {
        return actualLevel;
    }

    public void setActualLevel(int actualLevel) {
        this.actualLevel = actualLevel;
    }

    public double getActualHeight() {
        return actualHeight;
    }

    public void setActualHeight(double actualHeight) {
        this.actualHeight = actualHeight;
    }

    public boolean isEmergencyStopped() {
        return isEmergencyStopped;
    }

    public void setEmergencyStopped(boolean emergencyStopped) {
        isEmergencyStopped = emergencyStopped;
    }
}
