package models.partie_operative;

import models.MoteurTraction;

/**
 * Commande moteur gérant les arrêts d'urgence
 */
public class ArretUrgence extends CommandeSysteme {
    /**
     * Constructeur
     * @param engine moteur
     */
    public ArretUrgence(MoteurTraction engine) {
        super(engine);
        argsCount = 1;
    }

    @Override
    public void run() {
        System.out.println(args[0].equals("true") ? "Interruption du moteur." : "Reprise du moteur.");
        getEngine().setEmergencyStopped(Boolean.parseBoolean(args[0]));
    }
}
