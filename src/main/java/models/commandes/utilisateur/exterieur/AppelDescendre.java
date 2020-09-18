package models.commandes.utilisateur.exterieur;

import models.commandes.moteur.Descendre;
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
        this.linkedEngineCommand = Descendre.getInstance();
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
