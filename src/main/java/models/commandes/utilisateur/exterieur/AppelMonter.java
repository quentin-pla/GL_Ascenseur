package models.commandes.utilisateur.exterieur;

import models.commandes.moteur.Monter;
import models.commandes.utilisateur.CommandeUtilisateur;

public class AppelMonter extends CommandeUtilisateur {
    /**
     * Instance unique
     */
    protected static CommandeUtilisateur instance = null;

    /**
     * Constructeur
     */
    private AppelMonter() {
        this.linkedEngineCommand = Monter.getInstance();
    }

    /**
     * Récupérer l'instance unique
     * @return instance
     */
    public static CommandeUtilisateur getInstance() {
        if (instance == null) instance = new AppelMonter();
        return instance;
    }
}
