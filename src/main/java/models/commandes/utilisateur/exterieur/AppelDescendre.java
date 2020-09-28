package models.commandes.utilisateur.exterieur;

import models.Moteur;
import models.commandes.utilisateur.CommandeUtilisateur;

/**
 * Commande utilisateur pour appeler l'ascenseur et descendre
 */
public class AppelDescendre extends CommandeUtilisateur {
    /**
     * Constructeur
     */
    public AppelDescendre(Moteur engine) {
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