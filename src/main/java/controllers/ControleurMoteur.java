package controllers;

import models.Moteur;
import models.commandes.moteur.Descendre;
import models.commandes.moteur.Monter;
import models.commandes.moteur.OuverturePortes;
import models.commandes.utilisateur.exterieur.AppelDescendre;
import models.commandes.utilisateur.exterieur.AppelMonter;
import models.commandes.utilisateur.interieur.DemandeArretUrgence;
import models.commandes.utilisateur.interieur.DeplacementNiveau;
import views.VueAscenseur;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static java.lang.Thread.sleep;

public class ControleurMoteur implements PropertyChangeListener {
    /**
     * Mode de débugage
     */
    private boolean debugMode = true;

    /**
     * Vue ascenseur
     */
    private VueAscenseur view;

    /**
     * Moteur
     */
    private Moteur engine;

    /**
     * Constructeur par défaut
     * @param engine moteur
     */
    public ControleurMoteur(Moteur engine) {
        this.engine = engine;
        this.view = new VueAscenseur(engine.getLevels(), engine.getActualLevel().get());
        this.view.addChangeListener(this);
        this.engine.addChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String value = (String) evt.getNewValue();
        switch (evt.getPropertyName()) {
            case "animationRunning":
                manageAnimations(Boolean.parseBoolean(value));
                break;
            case "buttonInput":
                manageButtonInput(value);
                break;
            case "nextStop":
                manageNextStop(Integer.parseInt(value));
                break;
            case "actualLevel":
                manageLevelChange(Integer.parseInt(value));
                break;
            case "isOpen":
                manageDoorOpening(Boolean.parseBoolean(value));
                break;
            case "isEmergencyStopped":
                manageEmergencyStop(Boolean.parseBoolean(value));
                break;
            default:
                break;
        }
    }

    /**
     * Gérer les animations
     * @param value valeur
     */
    private void manageAnimations(boolean value) {
        if (!value) {
            if (debugMode) System.out.println("Niveau actuel: " + engine.getActualLevel());
            if (!OuverturePortes.getInstance().isLocked()) {
                if (Monter.getInstance().isLocked()) Monter.getInstance().unlock();
                else if (Descendre.getInstance().isLocked()) Descendre.getInstance().unlock();
            } else {
                OuverturePortes.getInstance().unlock();
            }
        }
    }

    /**
     * Gérer une saisie utilisateur
     * @param value valeur
     */
    private void manageButtonInput(String value) {
        boolean isNumeric;
        try {
            Integer.parseInt(value);
            isNumeric = true;
        } catch(NumberFormatException e){
            isNumeric = false;
        }
        if (isNumeric)
            DeplacementNiveau.getInstance().notifyEngine(value);
        else if (value.equals("⚠"))
            DemandeArretUrgence.getInstance().notifyEngine(!engine.isEmergencyStopped().get()+"");
        else if (value.length() > 1 && (value.contains("UP") || value.contains("DOWN"))) {
            try {
                Integer.parseInt(value.substring(0,1));
            } catch(NumberFormatException e){
                e.printStackTrace();
            }
            if (value.contains("UP"))
                AppelMonter.getInstance().notifyEngine(value.substring(0,1));
            else
                AppelDescendre.getInstance().notifyEngine(value.substring(0,1));
        }
    }

    /**
     * Gérer le prochain arrêt
     * @param value prochain arrêt
     */
    private void manageNextStop(int value) {
        if (engine.getActualLevel().get() == engine.getNextStop().get()) {
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            manageLevelChange(value);
        } else {
            if (Monter.getInstance().isLocked()) Monter.getInstance().unlock();
            else if (Descendre.getInstance().isLocked()) Descendre.getInstance().unlock();
            if (debugMode) {
                if (value != -1) System.out.println("Arrêt prochain niveau :" + value);
                else System.out.println("Attente du prochain arrêt.");
            }
        }
    }

    /**
     * Gérer le changement de niveau
     * @param value niveau
     */
    private void manageLevelChange(int value) {
        if (value != -1) view.getAscenseurCanvas().animateMove(value);
    }

    /**
     * Gérer l'animation d'ouverture des portes
     */
    private void manageDoorOpening(boolean isOpen) {
        if (isOpen) {
            view.getInButtonsCanvas().clearActiveButton(engine.getActualLevel().toString());
            String buttonToClear;
            if (!engine.getNextUpLevels().isEmpty() && !engine.getNextDownLevels().isEmpty() &&
                engine.getActualLevel().get() > 0 && engine.getActualLevel().get() < engine.getLevels())
                buttonToClear = engine.getActualLevel().toString() + engine.getActualDirection();
            else
                buttonToClear = engine.getActualLevel().toString();
            view.getOutButtonsCanvas().clearActiveButton(buttonToClear);
        }
        view.getAscenseurCanvas().setOpen(isOpen);
    }

    /**
     * Gérer arrêt urgence
     * @param value valeur
     */
    private void manageEmergencyStop(boolean value) {
        view.getAscenseurCanvas().setEmergencyStopped(value);
    }
}