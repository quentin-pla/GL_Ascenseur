package models.commandes.moteur;

import models.Moteur;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Commande moteur abstraite
 */
public abstract class CommandeMoteur implements Runnable {
    /**
     * Moteur lié
     */
    protected AtomicReference<Moteur> engine;

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
    public CommandeMoteur(Moteur engine) {
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
    protected int checkArgLevel(String level) {
        int calledLevel = 0;
        try {
            calledLevel = Integer.parseInt(level);
            if (calledLevel < 0 || calledLevel > getEngine().getLevels())
                throw new Exception("Niveau invalide");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calledLevel;
    }

    /*** GETTERS & SETTERS ***/

    public Moteur getEngine() {
        return engine.get();
    }

    public String[] getArgs() { return args; }

    public void setArgs(String... args) {
        this.args = args;
        checkArgsLength();
    }
}
