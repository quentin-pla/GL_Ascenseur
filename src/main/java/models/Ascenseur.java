package models;

import controllers.ControleurAscenseur;
import models.demandes_utilisateurs.DemandeUtilisateur;
import models.demandes_utilisateurs.externes.AppelDescendre;
import models.demandes_utilisateurs.externes.AppelMonter;
import models.demandes_utilisateurs.internes.DemandeArretUrgence;
import models.demandes_utilisateurs.internes.DeplacementNiveau;
import views.VueAscenseur;

import java.util.ArrayList;

/***
 * Ascenseur
 */
public class Ascenseur {
    /**
     * Moteur
     */
    private MoteurTraction engine;

    /**
     * Affichage graphique
     */
    private VueAscenseur view;

    /**
     * Liste des commandes utilisateur
     */
    private ArrayList<DemandeUtilisateur> commands;

    /**
     * Constructeur par défaut
     * @param levels niveaux
     */
    public Ascenseur(int levels, int defaultLevel) {
        this.engine = new MoteurTraction(levels, defaultLevel);
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

    public MoteurTraction getEngine() { return engine; }

    public VueAscenseur getView() { return view; }

    public DemandeUtilisateur getAppelDescendre() { return commands.get(0); }

    public DemandeUtilisateur getAppelMonter() { return commands.get(1); }

    public DemandeUtilisateur getDemandeArretUrgence() { return commands.get(2); }

    public DemandeUtilisateur getDeplacementNiveau() { return commands.get(3); }
}