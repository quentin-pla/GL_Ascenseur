package models.commandes.moteur;

import models.Moteur;

import java.util.concurrent.atomic.AtomicReference;

public class Descendre extends CommandeMoteur {
    /**
     * Instance unique
     */
    private static AtomicReference<Descendre> instance = null;

    /**
     * Récupérer l'instance unique
     * @return instance
     */
    public static CommandeMoteur getInstance() {
        if (instance == null) instance = new AtomicReference<>(new Descendre());
        return instance.get();
    }

    @Override
    public void run() {
        while (true) {
            if (!getEngine().getIsOpen().get() &&
                getEngine().getNextStop().get() != -1 &&
                getEngine().getActualLevel().get() > 0 &&
                getEngine().getActualDirection().get() == Moteur.Direction.DOWN) {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getEngine().getActualLevel().getAndDecrement();
                System.out.println("Descente de l'ascenseur: " + getEngine().getActualLevel());
            }
        }
    }
}
