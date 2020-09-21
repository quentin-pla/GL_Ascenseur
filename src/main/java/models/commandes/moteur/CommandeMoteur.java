package models.commandes.moteur;

import models.Moteur;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

public abstract class CommandeMoteur extends Thread {
    /**
     * Moteur lié
     */
    protected AtomicReference<Moteur> engine;

    /**
     * Arguments passés en paramètre
     */
    protected LinkedBlockingQueue<String[]> args;

    /**
     * Lier un moteur
     * @param engine moteur
     */
    public void linkEngine(Moteur engine) {
        this.engine = new AtomicReference<>(engine);
        this.args = new LinkedBlockingQueue<>();
        this.start();
    }

    /**
     * Exécuter la commande sur le moteur
     */
    public void execute(String... args) {
        this.args.add(args);
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

    public LinkedBlockingQueue<String[]> getArgs() {
        return args;
    }
}
