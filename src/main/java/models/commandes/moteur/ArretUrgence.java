package models.commandes.moteur;

import models.Moteur;

/**
 * Commande moteur gérant les arrêts d'urgence
 */
public class ArretUrgence extends CommandeMoteur {
    /**
     * Constructeur
     * @param engine moteur
     */
    public ArretUrgence(Moteur engine) {
        super(engine);
        argsCount = 1;
    }

    @Override
    public void run() {
        System.out.println(args[0].equals("true") ? "Interruption du moteur." : "Reprise du moteur.");
        getEngine().setEmergencyStopped(Boolean.parseBoolean(args[0]));
    }
}
