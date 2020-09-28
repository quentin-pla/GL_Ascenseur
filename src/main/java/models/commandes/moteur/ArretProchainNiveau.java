package models.commandes.moteur;

import models.Moteur;

/**
 * Commande moteur pour gérer les arrêts aux prochains niveaux
 */
public class ArretProchainNiveau extends CommandeMoteur {
    /**
     * Constructeur
     * @param engine moteur
     */
    public ArretProchainNiveau(Moteur engine) {
        super(engine);
        argsCount = 2;
    }

    @Override
    public void run() {
        //Premier argument : niveau
        //Deuxième argument : direction
        getEngine().addNextLevel(checkArgLevel(args[0]), args[1]);
    }
}
