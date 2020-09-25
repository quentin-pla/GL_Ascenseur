package models.commandes.moteur;

import java.util.concurrent.atomic.AtomicReference;

public class ArretProchainNiveau extends CommandeMoteur {
    /**
     * Instance unique
     */
    private static AtomicReference<ArretProchainNiveau> instance = null;

    /**
     * Récupérer l'instance unique
     * @return instance
     */
    public static CommandeMoteur getInstance() {
        if (instance == null) {
            instance = new AtomicReference<>(new ArretProchainNiveau());
            instance.get().setName("ArretProchainNiveau");
        }
        return instance.get();
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Deux arguments passés contenant la direction et le niveau
            if (!args.isEmpty() && args.element().length == 2) {
                final String[] actualArgs = args.remove();
                getEngine().addNextLevel(checkArgLevel(actualArgs[0]), actualArgs[1]);
            }
        }
    }
}
