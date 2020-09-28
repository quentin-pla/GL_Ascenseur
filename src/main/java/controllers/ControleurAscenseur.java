package controllers;

import models.Ascenseur;
import models.Moteur;
import views.VueAscenseur;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Controleur de l'ascenseur
 */
public class ControleurAscenseur implements PropertyChangeListener {
    /**
     * Mode de débugage
     */
    private boolean debugMode = true;

    /**
     * Vue ascenseur
     */
    private Ascenseur ascenseur;

    /**
     * Constructeur par défaut
     * @param ascenseur ascenseur
     */
    public ControleurAscenseur(Ascenseur ascenseur) {
        this.ascenseur = ascenseur;
        getView().addChangeListener(this);
        getEngine().addChangeListener(this);
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
     * @param inProgress valeur
     */
    private void manageAnimations(boolean inProgress) {
        if (!inProgress) {
            if (debugMode) System.out.println("Niveau actuel: " + getEngine().getActualLevel());
            if (getEngine().getNextStop().get() == getEngine().getActualLevel().get()) {
                getEngine().executeOuverturePortes();
            } else {
                if (getEngine().getActualDirection().get().equals("UP")) getEngine().executeMonter();
                else getEngine().executeDescendre();
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
            ascenseur.getDeplacementNiveau().notifyEngine(value);
        else if (value.equals("⚠"))
            ascenseur.getDemandeArretUrgence().notifyEngine(!getEngine().isEmergencyStopped().get()+"");
        else if (value.length() > 1 && (value.contains("UP") || value.contains("DOWN"))) {
            try {
                Integer.parseInt(value.substring(0,1));
            } catch(NumberFormatException e){
                e.printStackTrace();
            }
            if (value.contains("UP"))
                ascenseur.getAppelMonter().notifyEngine(value.substring(0,1));
            else
                ascenseur.getAppelDescendre().notifyEngine(value.substring(0,1));
        }
    }

    /**
     * Gérer le prochain arrêt
     * @param value prochain arrêt
     */
    private void manageNextStop(int value) {
        if (getEngine().getActualLevel().get() == getEngine().getNextStop().get()) {
            manageLevelChange(value);
        } else {
            if (getEngine().getActualDirection().get().equals("UP")) getEngine().executeMonter();
            else getEngine().executeDescendre();
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
        if (value != -1) getView().getAscenseurCanvas().animateMove(value);
    }

    /**
     * Gérer l'animation d'ouverture des portes
     */
    private void manageDoorOpening(boolean isOpen) {
        if (isOpen) {
            getView().getInButtonsCanvas().clearActiveButton(getEngine().getActualLevel().toString());
            String buttonToClear;
            if (!getEngine().getNextUpLevels().isEmpty() && !getEngine().getNextDownLevels().isEmpty() &&
                getEngine().getActualLevel().get() > 0 && getEngine().getActualLevel().get() < getEngine().getLevels())
                buttonToClear = getEngine().getActualLevel().toString() + getEngine().getActualDirection();
            else
                buttonToClear = getEngine().getActualLevel().toString();
            getView().getOutButtonsCanvas().clearActiveButton(buttonToClear);
        }
        getView().getAscenseurCanvas().setOpen(isOpen);
    }

    /**
     * Gérer arrêt urgence
     * @param value valeur
     */
    private void manageEmergencyStop(boolean value) {
        getView().getAscenseurCanvas().setEmergencyStopped(value);
    }

    /*** GETTERS & SETTERS ***/

    private Moteur getEngine() { return ascenseur.getEngine(); }

    private VueAscenseur getView() { return ascenseur.getView(); }
}