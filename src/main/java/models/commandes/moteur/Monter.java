package models.commandes.moteur;

import models.Moteur;

/**
 * Commande moteur pour monter l'ascenseur
 */
public class Monter extends CommandeMoteur {
    /**
     * Constructeur
     * @param engine moteur
     */
    public Monter(Moteur engine) {
        super(engine);
    }

    @Override
    public void run() {
        if (!getEngine().getIsOpen().get() &&
            getEngine().getNextStop().get() != -1 &&
            getEngine().getActualLevel().get() < getEngine().getLevels() &&
            getEngine().getActualDirection().get().equals("UP")) {
            getEngine().incrementActualLevel();
        }
    }
}
