package models.commandes.utilisateur.exterieur;

import models.Moteur;
import models.commandes.moteur.ArretProchainNiveau;
import models.commandes.utilisateur.CommandeUtilisateur;

public class AppelDescendre extends CommandeUtilisateur {
    /**
     * Instance unique
     */
    protected static AppelDescendre instance = null;

    /**
     * Constructeur
     */
    private AppelDescendre() {
        this.linkedEngineCommand = ArretProchainNiveau.getInstance();
        argsCount = 1; //Un argument pour savoir depuis quel niveau l'appel a été spécifié
        preArgs.add(Moteur.Direction.DOWN.toString()); //Direction souhaitée
    }

    /**
     * Récupérer l'instance unique
     * @return instance
     */
    public static CommandeUtilisateur getInstance() {
        if (instance == null) instance = new AppelDescendre();
        return instance;
    }
}
