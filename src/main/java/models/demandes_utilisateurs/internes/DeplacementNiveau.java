package models.demandes_utilisateurs.internes;

import models.MoteurTraction;
import models.demandes_utilisateurs.DemandeUtilisateur;

/**
 * Commande moteur pour se déplacer à un niveau spécifique
 */
public class DeplacementNiveau extends DemandeUtilisateur {
    /**
     * Constructeur
     */
    public DeplacementNiveau(MoteurTraction engine) {
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