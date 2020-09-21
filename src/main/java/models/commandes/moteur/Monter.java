package models.commandes.moteur;

import models.Moteur;

import java.util.concurrent.atomic.AtomicReference;

public class Monter extends CommandeMoteur {
    /**
     * Instance unique
     */
    private static AtomicReference<Monter> instance = null;

    /**
     * Récupérer l'instance unique
     * @return instance
     */
    public static CommandeMoteur getInstance() {
        if (instance == null) instance = new AtomicReference<>(new Monter());
        return instance.get();
    }

    @Override
    public void run() {
        while (true) {
            if (!getEngine().getIsOpen().get() &&
                getEngine().getNextStop().get() != -1 &&
                getEngine().getActualLevel().get() < getEngine().getLevels() &&
                getEngine().getActualDirection().get() == Moteur.Direction.UP) {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getEngine().getActualLevel().getAndIncrement();
                System.out.println("Montée de l'ascenseur: " + getEngine().getActualLevel());
            }
        }
    }
}
