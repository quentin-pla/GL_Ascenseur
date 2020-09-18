package models.commandes.utilisateur.interieur;

import models.commandes.moteur.ArretUrgence;
import models.commandes.utilisateur.CommandeUtilisateur;

public class DemandeArretUrgence extends CommandeUtilisateur {
    /**
     * Instance unique
     */
    protected static DemandeArretUrgence instance = null;

    /**
     * Constructeur
     */
    private DemandeArretUrgence() {
        this.linkedEngineCommand = ArretUrgence.getInstance();
    }

    /**
     * Récupérer l'instance unique
     * @return instance
     */
    public static CommandeUtilisateur getInstance() {
        if (instance == null) instance = new DemandeArretUrgence();
        return instance;
    }
}
