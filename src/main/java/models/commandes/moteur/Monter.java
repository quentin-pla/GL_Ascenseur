package models.commandes.moteur;

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
        if (instance == null) {
            instance = new AtomicReference<>(new Monter());
            instance.get().setName("Monter");
        }
        return instance.get();
    }

    @Override
    public void run() {
        while (true) {
            if (!getEngine().getIsOpen().get() &&
                getEngine().getNextStop().get() != -1 &&
                getEngine().getActualLevel().get() < getEngine().getLevels() &&
                getEngine().getActualDirection().get().equals("UP")) {
                getEngine().incrementActualLevel();
                lock();
            }
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
