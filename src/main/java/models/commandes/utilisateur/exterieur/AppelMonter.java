package models.commandes.utilisateur.exterieur;

import models.Moteur;
import models.commandes.utilisateur.CommandeUtilisateur;

/**
 * Commande utilisateur pour appeler l'ascenseur et monter
 */
public class AppelMonter extends CommandeUtilisateur {
    /**
     * Constructeur
     */
    public AppelMonter(Moteur engine) {
        super(engine);
        argsCount = 1; //Un argument pour savoir depuis quel niveau l'appel a été spécifié
        preArgs.add("UP"); //Direction souhaitée
    }

    @Override
    public void notifyEngine(String... args) {
        checkArgsLength(args);
        engine.executeArretProchainNiveau(getFinalArgs(args));
    }
}