package models.demandes_utilisateurs.internes;

import models.MoteurTraction;
import models.demandes_utilisateurs.DemandeUtilisateur;

/**
 * Commande utilisateur pour demander un arrêt d'urgence
 */
public class DemandeArretUrgence extends DemandeUtilisateur {
    /**
     * Constructeur
     */
    public DemandeArretUrgence(MoteurTraction engine) {
        super(engine);
        argsCount = 1;
    }

    @Override
    public void notifyEngine(String... args) {
        checkArgsLength(args);
        engine.executeArretUrgence(getFinalArgs(args));
    }
}