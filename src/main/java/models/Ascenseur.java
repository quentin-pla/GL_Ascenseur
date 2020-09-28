package models;

import controllers.ControleurAscenseur;
import models.commandes.utilisateur.CommandeUtilisateur;
import models.commandes.utilisateur.exterieur.AppelDescendre;
import models.commandes.utilisateur.exterieur.AppelMonter;
import models.commandes.utilisateur.interieur.DemandeArretUrgence;
import models.commandes.utilisateur.interieur.DeplacementNiveau;
import views.VueAscenseur;

import java.util.ArrayList;

/***
 * Ascenseur
 */
public class Ascenseur {
    /**
     * Moteur
     */
    private Moteur engine;

    /**
     * Affichage graphique
     */
    private VueAscenseur view;

    /**
     * Liste des commandes utilisateur
     */
    private ArrayList<CommandeUtilisateur> commands;

    /**
     * Constructeur par défaut
     * @param levels niveaux
     */
    public Ascenseur(int levels, int defaultLevel) {
        this.engine = new Moteur(levels, defaultLevel);
        this.view = new VueAscenseur(levels, defaultLevel);
        this.commands = new ArrayList<>();
        initCommands();
    }

    /**
     * Initialisation des commandes utilisateur
     */
    private void initCommands() {
        commands.add(new AppelDescendre(engine));
        commands.add(new AppelMonter(engine));
        commands.add(new DemandeArretUrgence(engine));
        commands.add(new DeplacementNiveau(engine));
    }

    /**
     * Main
     * @param args arguments
     */
    public static void main(String[] args) {
        Ascenseur ascenseur = new Ascenseur(4,0);
        new ControleurAscenseur(ascenseur);
    }

    /*** GETTERS & SETTERS ***/

    public Moteur getEngine() { return engine; }

    public VueAscenseur getView() { return view; }

    public CommandeUtilisateur getAppelDescendre() { return commands.get(0); }

    public CommandeUtilisateur getAppelMonter() { return commands.get(1); }

    public CommandeUtilisateur getDemandeArretUrgence() { return commands.get(2); }

    public CommandeUtilisateur getDeplacementNiveau() { return commands.get(3); }
}