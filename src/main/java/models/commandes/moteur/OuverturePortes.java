package models.commandes.moteur;

import java.util.concurrent.atomic.AtomicReference;

public class OuverturePortes extends CommandeMoteur {
    /**
     * Instance unique
     */
    private static AtomicReference<OuverturePortes> instance = null;

    /**
     * Récupérer l'instance unique
     * @return instance
     */
    public static CommandeMoteur getInstance() {
        if (instance == null) {
            instance = new AtomicReference<>(new OuverturePortes());
            instance.get().setName("OuverturePortes");
        }
        return instance.get();
    }

    @Override
    public void run() {
        while (true) {
            if (getEngine().getNextStop().get() == getEngine().getActualLevel().get()) {
                lock();
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getEngine().setIsOpen(true);
                System.out.println("<- Ouverture ->");
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getEngine().setIsOpen(false);
                System.out.println("-> Fermeture <-");
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getEngine().levelPerformed();
            }
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
