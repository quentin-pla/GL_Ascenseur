package models.commandes.moteur;

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
        if (instance == null) {
            instance = new AtomicReference<>(new Descendre());
            instance.get().setName("Descendre");
        }
        return instance.get();
    }

    @Override
    public void run() {
        while (true) {
            if (!getEngine().getIsOpen().get() &&
                getEngine().getNextStop().get() != -1 &&
                getEngine().getActualLevel().get() > 0 &&
                getEngine().getActualDirection().get().equals("DOWN")) {
                getEngine().decrementActualLevel();
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
