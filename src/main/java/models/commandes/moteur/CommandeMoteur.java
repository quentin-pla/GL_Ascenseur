package models.commandes.moteur;

import models.Moteur;

public abstract class CommandeMoteur {
    /**
     * Moteur lié
     */
    protected Moteur engine;

    /**
     * Lier un moteur
     * @param engine moteur
     */
    public void linkEngine(Moteur engine) {
        this.engine = engine;
    }

    /**
     * Exécuter la commande sur le moteur
     */
    public abstract void execute();
}
