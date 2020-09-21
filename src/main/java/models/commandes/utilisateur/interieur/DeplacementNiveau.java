package models.commandes.utilisateur.interieur;

import models.Moteur;
import models.commandes.moteur.ArretProchainNiveau;
import models.commandes.utilisateur.CommandeUtilisateur;

public class DeplacementNiveau extends CommandeUtilisateur {
    /**
     * Instance unique
     */
    protected static DeplacementNiveau instance = null;

    /**
     * Constructeur
     */
    private DeplacementNiveau() {
        linkedEngineCommand = ArretProchainNiveau.getInstance();
        argsCount = 1;
        preArgs.add(Moteur.Direction.NONE.toString()); //Direction
    }

    /**
     * Récupérer l'instance unique
     * @return instance
     */
    public static CommandeUtilisateur getInstance() {
        if (instance == null) instance = new DeplacementNiveau();
        return instance;
    }
}
