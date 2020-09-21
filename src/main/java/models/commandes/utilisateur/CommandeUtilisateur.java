package models.commandes.utilisateur;

import models.commandes.moteur.CommandeMoteur;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class CommandeUtilisateur {
    /**
     * Commande moteur reliée
     */
    protected CommandeMoteur linkedEngineCommand = null;

    /**
     * Paramètres prédéfinis
     */
    protected ArrayList<String> preArgs = new ArrayList<>();

    /**
     * Nombre d'arguments nécessaires pour exécuter une commande
     */
    protected int argsCount = 0;

    /**
     * Notifier le moteur de la demande
     */
    public void notifyEngine(String... args) {
        List<String> finalArgs = new ArrayList<>(Arrays.asList(args));
        finalArgs.addAll(preArgs);
        try {
            if (args.length == argsCount) linkedEngineCommand.execute(finalArgs.toArray(new String[finalArgs.size()]));
            else throw new Exception("Nombre d'arguments incorrect pour exécuter la commande.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
