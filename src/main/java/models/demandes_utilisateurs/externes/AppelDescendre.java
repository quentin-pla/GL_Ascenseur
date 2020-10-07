package models.demandes_utilisateurs.externes;

import models.MoteurTraction;
import models.demandes_utilisateurs.DemandeUtilisateur;

/**
 * Commande utilisateur pour appeler l'ascenseur et descendre
 */
public class AppelDescendre extends DemandeUtilisateur {
    /**
     * Constructeur
     */
    public AppelDescendre(MoteurTraction engine) {
        super(engine);
        argsCount = 1; //Un argument pour savoir depuis quel niveau l'appel a été spécifié
        preArgs.add("DOWN"); //Direction souhaitée
    }

    @Override
    public void notifyEngine(String... args) {
        checkArgsLength(args);
        engine.executeArretProchainNiveau(getFinalArgs(args));
    }
}