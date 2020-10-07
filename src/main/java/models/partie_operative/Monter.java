package models.partie_operative;

import models.MoteurTraction;

import static java.lang.Thread.sleep;

/**
 * Commande moteur pour monter l'ascenseur
 */
public class Monter extends CommandeSysteme {
    /**
     * Constructeur
     * @param engine moteur
     */
    public Monter(MoteurTraction engine) {
        super(engine);
    }

    @Override
    public void run() {
        while (!getEngine().getIsOpen().get() &&
            getEngine().getNextLevel() != -1 &&
            getEngine().getActualPosition().get() <= getEngine().getLevels() * 100 &&
            getEngine().getActualDirection().get().equals("UP")) {
            if (!getEngine().isEmergencyStopped().get())
                getEngine().incrementActualPosition();
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
