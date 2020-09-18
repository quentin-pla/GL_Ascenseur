package models;

import models.commandes.utilisateur.CommandeUtilisateur;

public class Bouton {
    /**
     * Commande utilisateur
     */
    private CommandeUtilisateur userCommand;

    /**
     * Constructeur par défaut
     * @param userCommand commande utilisateur
     */
    public Bouton(CommandeUtilisateur userCommand) {
        this.userCommand = userCommand;
    }

    /**
     * Simulation d'un appui sur le bouton
     */
    public void click() {
        userCommand.notifyEngine();
    }
}
