package models.commandes.utilisateur.interieur;

import models.Moteur;
import models.commandes.utilisateur.CommandeUtilisateur;

/**
 * Commande utilisateur pour demander un arrêt d'urgence
 */
public class DemandeArretUrgence extends CommandeUtilisateur {
    /**
     * Constructeur
     */
    public DemandeArretUrgence(Moteur engine) {
        super(engine);
        argsCount = 1;
    }

    @Override
    public void notifyEngine(String... args) {
        checkArgsLength(args);
        engine.executeArretUrgence(getFinalArgs(args));
    }
}