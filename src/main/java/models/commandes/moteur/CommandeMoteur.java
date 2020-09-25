package models.commandes.moteur;

import models.Moteur;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

public abstract class CommandeMoteur extends Thread {
    /**
     * Mode de débugage
     */
    private boolean debugMode = false;

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
        start();
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

    /**
     * Verrouiller le thread
     */
    synchronized public void lock() {
        try {
            if (debugMode) System.out.println(getName() + " LOCKED");
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Déverouiller le thread
     */
    synchronized public void unlock() {
        if (debugMode) System.out.println(getName() + " UNLOCKED");
        notify();
    }

    /**
     * Savoir si le thread est verrouillé ou pas
     * @return état du thread
     */
    public boolean isLocked() {
        return getState().toString().equals("WAITING");
    }

    /*** GETTERS & SETTERS ***/

    public Moteur getEngine() {
        return engine.get();
    }
}
