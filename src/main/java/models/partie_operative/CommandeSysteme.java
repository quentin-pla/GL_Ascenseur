package models.partie_operative;

import models.MoteurTraction;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Commande moteur abstraite
 */
public abstract class CommandeSysteme implements Runnable {
    /**
     * Moteur lié
     */
    protected AtomicReference<MoteurTraction> engine;

    /**
     * Arguments passés en paramètre
     */
    protected String[] args;

    /**
     * Nombre d'arguments autorisés
     */
    protected int argsCount;

    /**
     * Constructeur
     * @param engine moteur
     */
    public CommandeSysteme(MoteurTraction engine) {
        this.engine = new AtomicReference<>(engine);
        this.args = new String[]{};
        this.argsCount = 0;
    }

    /**
     * Vérifier le nombre d'arguments passé en paramètre
     */
    protected void checkArgsLength() {
        try {
            if (args.length != argsCount) throw new Exception("Nombre d'arguments invalide");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Vérifier un niveau passé en paramètre avant d'exécuter une commande moteur
     * @param level niveau
     * @return niveau vérifié et convertit en entier
     */
    protected boolean checkArgLevel(String level) {
        int calledLevel;
        try {
            calledLevel = Integer.parseInt(level);
            if (calledLevel < 0 || calledLevel > getEngine().getLevels())
                throw new Exception("Niveau invalide");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /*** GETTERS & SETTERS ***/

    public MoteurTraction getEngine() {
        return engine.get();
    }

    public void setArgs(String... args) {
        this.args = args;
        checkArgsLength();
    }
}
