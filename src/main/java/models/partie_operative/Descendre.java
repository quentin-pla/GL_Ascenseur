package models.partie_operative;

import models.MoteurTraction;

import static java.lang.Thread.sleep;

/**
 * Commande moteur pour descendre l'ascenseur
 */
public class Descendre extends CommandeSysteme {
    /**
     * Constructeur
     * @param engine moteur
     */
    public Descendre(MoteurTraction engine) {
        super(engine);
    }

    @Override
    public void run() {
        while (!getEngine().getIsOpen().get() &&
            getEngine().getNextLevel() != -1 &&
            getEngine().getActualPosition().get() >= 0 &&
            getEngine().getActualDirection().get().equals("DOWN")) {
            if (!getEngine().isEmergencyStopped().get())
                getEngine().decrementActualPosition();
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
