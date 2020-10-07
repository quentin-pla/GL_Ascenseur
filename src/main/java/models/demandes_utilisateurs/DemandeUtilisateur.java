package models.demandes_utilisateurs;

import models.MoteurTraction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Commande utilisateur abstraite
 */
public abstract class DemandeUtilisateur {
    /**
     * Moteur relié
     */
    protected MoteurTraction engine;

    /**
     * Paramètres prédéfinis
     */
    protected ArrayList<String> preArgs;

    /**
     * Nombre d'arguments nécessaires pour exécuter une commande
     */
    protected int argsCount = 0;

    /**
     * Constructeur
     * @param engine moteur
     */
    public DemandeUtilisateur(MoteurTraction engine) {
        this.engine = engine;
        this.preArgs = new ArrayList<>();
    }

    /**
     * Vérifier le nombre d'arguments passé en paramètre
     */
    protected void checkArgsLength(String... args) {
        try {
            if (args.length != argsCount) throw new Exception("Nombre d'arguments invalide");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Récupérer la liste des arguments complète
     * @param args arguments passés en paramètre
     * @return liste finale des arguments
     */
    protected String[] getFinalArgs(String... args) {
        List<String> finalArgs = new ArrayList<>(Arrays.asList(args));
        finalArgs.addAll(preArgs);
        return finalArgs.toArray(new String[finalArgs.size()]);
    }

    /**
     * Notifier le moteur de la demande
     */
    public abstract void notifyEngine(String... args);
}