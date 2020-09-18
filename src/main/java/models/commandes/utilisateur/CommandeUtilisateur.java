package models.commandes.utilisateur;

import models.commandes.moteur.CommandeMoteur;

public abstract class CommandeUtilisateur {
    /**
     * Commande moteur reliée
     */
    protected CommandeMoteur linkedEngineCommand = null;

    /**
     * Notifier le moteur de la demande
     */
    public void notifyEngine() {
        linkedEngineCommand.execute();
    }
}
