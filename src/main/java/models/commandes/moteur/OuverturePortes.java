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
        if (instance == null) instance = new AtomicReference<>(new OuverturePortes());
        return instance.get();
    }

    @Override
    public void run() {
        while (true) {
            if (getEngine().getIsOpen().get()) {
                try {
                    sleep(1000);
                    System.out.println("-> Ouverture des portes");
                    sleep(2000);
                    System.out.println("-> Fermeture des portes");
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getEngine().setIsOpen(false);
            }
        }
    }
}
