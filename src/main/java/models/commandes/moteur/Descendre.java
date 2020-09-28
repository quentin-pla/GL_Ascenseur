package models.commandes.moteur;

import models.Moteur;

/**
 * Commande moteur pour descendre l'ascenseur
 */
public class Descendre extends CommandeMoteur {
    /**
     * Constructeur
     * @param engine moteur
     */
    public Descendre(Moteur engine) {
        super(engine);
    }

    @Override
    public void run() {
        if (!getEngine().getIsOpen().get() &&
            getEngine().getNextStop().get() != -1 &&
            getEngine().getActualLevel().get() > 0 &&
            getEngine().getActualDirection().get().equals("DOWN")) {
            getEngine().decrementActualLevel();
        }
    }
}
