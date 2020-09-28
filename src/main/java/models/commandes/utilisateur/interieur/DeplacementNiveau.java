package models.commandes.utilisateur.interieur;

import models.Moteur;
import models.commandes.utilisateur.CommandeUtilisateur;

/**
 * Commande moteur pour se déplacer à un niveau spécifique
 */
public class DeplacementNiveau extends CommandeUtilisateur {
    /**
     * Constructeur
     */
    public DeplacementNiveau(Moteur engine) {
        super(engine);
        argsCount = 1;
        preArgs.add(""); //Direction
    }

    @Override
    public void notifyEngine(String... args) {
        checkArgsLength(args);
        engine.executeArretProchainNiveau(getFinalArgs(args));
    }
}