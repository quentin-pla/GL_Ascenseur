package controllers;

import models.Ascenseur;
import models.MoteurTraction;
import views.VueAscenseur;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Controleur de l'ascenseur
 */
public class ControleurAscenseur implements PropertyChangeListener {
    /**
     * Ascenseur
     */
    private Ascenseur ascenseur;

    /**
     * Constructeur par défaut
     * @param ascenseur ascenseur
     */
    public ControleurAscenseur(Ascenseur ascenseur) {
        this.ascenseur = ascenseur;
        getEngine().addChangeListener(this);
        getView().addChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String value = (String) evt.getNewValue();
        switch (evt.getPropertyName()) {
            case "buttonInput":
                manageButtonInput(value);
                break;
            case "actualPosition":
                managePositionChange(Integer.parseInt(value));
                break;
            case "isOpen":
                manageDoorOpening(Boolean.parseBoolean(value));
                break;
            default:
                break;
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
     * Gérer le changement de niveau
     * @param value niveau
     */
    private void managePositionChange(int value) {
        getView().getAscenseurCanvas().setAscenseurYPosition(value);
    }

    /**
     * Gérer l'animation d'ouverture des portes
     */
    private void manageDoorOpening(boolean isOpen) {
        if (isOpen) {
            getView().getInButtonsCanvas().clearActiveButton(getEngine().getActualLevel().toString());
            getView().getOutButtonsCanvas().clearActiveButton(getEngine().getActualLevel().toString());
        }
        getView().getAscenseurCanvas().setOpen(isOpen);
    }

    /*** GETTERS & SETTERS ***/

    private MoteurTraction getEngine() { return ascenseur.getEngine(); }

    private VueAscenseur getView() { return ascenseur.getView(); }
}