package models.commandes.utilisateur.exterieur;

import models.commandes.moteur.ArretProchainNiveau;
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
        this.linkedEngineCommand = ArretProchainNiveau.getInstance();
        argsCount = 1; //Un argument pour savoir depuis quel niveau l'appel a été spécifié
        preArgs.add("UP"); //Direction souhaitée
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